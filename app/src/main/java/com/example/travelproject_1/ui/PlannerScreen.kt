package com.example.travelproject_1.ui

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelproject_1.BuildConfig
import com.example.travelproject_1.R
import com.example.travelproject_1.components.SelectButton
import com.example.travelproject_1.data.MapBtnNames
import com.example.travelproject_1.data.Place
import com.example.travelproject_1.model.MapButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PlannerScreen(
    planViewModel: PlannerViewModel = viewModel(),
    navigateToMemory: () -> Unit = {}
){
    val uiState by planViewModel.uiState.collectAsState()

    val current = LocalContext.current

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MapViewShow(
            markerStartLocation = planViewModel.startLocationLatLng,
            markerEndLocation = planViewModel.endLocationLatLng,
            markerStopLocations = "",
            cameraPositionState = CameraPositionState(
                position = CameraPosition.fromLatLngZoom(
                    planViewModel.cameraLocationLatLang,
                    9f)
            ),
            drawPolyList = uiState.path,
            addPath = {
                planViewModel.addPath(it)
            },
            isStartEndGiven = uiState.pathShow,
            cameraBounds = uiState.bounds,
            onLocationTypeChange = {
                planViewModel.clickLocationType(it)
            },
            locationType = planViewModel.locationType,
            middleLocation = planViewModel.middleLocationLatLang,
            addLocations = {
                planViewModel.setNearbyPlaces(it)
            },
            middleLocationShow = uiState.middlePlaceShow,
            middleLocations = planViewModel.placesNearByType,
            radiusOfSearch = planViewModel.distanceStartToEnd,
            addMarkerToPath = {
                planViewModel.addLocationStandby(it)
            },
            setMiddlePlaceToList = {
                planViewModel.addLocationToPath()
            }
        )

        ScheduleShow(
            startLocation = planViewModel.enteredStartLocation,
            endLocation = planViewModel.enteredEndLocation,
            locationsList = planViewModel.locationsAdded,
            isStartLocationChange = {
                planViewModel.addStartLocation(it)
            },
            isEndLocationChange = {
                planViewModel.addEndLocation(it)
            },
            isSavePressed = {},
            isStartButtonPressed = {
                planViewModel.setStartLocation(
                    getStartLocation(current, planViewModel.enteredStartLocation)
                )
            },
            isEndButtonPressed = {
                planViewModel.setEndLocation(
                    getStartLocation(current, planViewModel.enteredEndLocation)
                )
            },
            isSaveButtonPressed = {
                planViewModel.checkAndRunDatabaseInsertion()
            },
            goToMemory = {
                navigateToMemory()
            }
        )

        if(planViewModel.showAlertAlreadyAdded){
            AlertDialog(
                onDismissRequest = { planViewModel.showAlertAlreadyAdded = false },
                confirmButton = {
                    Button(
                        onClick = { planViewModel.showAlertAlreadyAdded = false }
                    ) {
                        Text(text = "Got it")
                    }
                },
                title = {
                    Text(text = "Location Add")
                },
                text = {
                    Text(text = "Location you are currently trying to add already added on the path")
                })
        }

    }
}

@Composable
fun MapViewShow(
    markerStartLocation: LatLng,
    markerEndLocation: LatLng,
    markerStopLocations: String,
    drawPolyList: List<LatLng>,
    cameraPositionState: CameraPositionState,
    addPath: (List<LatLng>) -> Unit,
    isStartEndGiven: Boolean,
    cameraBounds: LatLngBounds,
    onLocationTypeChange: (Int) -> Unit,
    locationType: Int,
    middleLocation: LatLng,
    addLocations: (List<Place>) -> Unit,
    middleLocationShow: Boolean,
    middleLocations: List<Place>,
    radiusOfSearch: Int,
    addMarkerToPath: (Place) -> Unit,
    setMiddlePlaceToList: () -> Unit
){
    val interactionSource = remember {
        MutableInteractionSource()
    }

    var placeBoxShow by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .height(500.dp)
        .fillMaxWidth()){

        if(isStartEndGiven){
            LocationButtons(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 5.dp, end = 5.dp)
                    .zIndex(2F),
                onLocationTypeChanged = {
                    onLocationTypeChange(it)
                })
        }

        GoogleMap(
            modifier = Modifier
                .height(500.dp)
                .zIndex(1F),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = markerStartLocation),
                title = "Start",
                snippet = "Marker in Start"
            )

            Marker(
                state = MarkerState(position = markerEndLocation),
                title = "End",
                snippet = "Marker in End"
            )

            if(isStartEndGiven && !middleLocationShow){
                val key = BuildConfig.MAPS_API_KEY
                getDirectionsPoly(
                    key,
                    markerStartLocation,
                    markerEndLocation){
                        decodedPath -> addPath(decodedPath)
                }

                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(cameraBounds,100)
                cameraPositionState.move(cameraUpdate)

                fetchPlacesNearPolyline(
                    key,
                    middleLocation,
                    radiusOfSearch){

                    places ->  addLocations(places)
                }
            }

            Polyline(points = drawPolyList)

            if(middleLocationShow){
                for (place in middleLocations){
                    MarkerInfoWindowContent(
                        state = MarkerState(position = place.placeLocation!!),
                        title = place.placeName,
                        snippet = place.placeName,
                        content = {
                            addMarkerToPath(place)
                            placeBoxShow = true
                            cardInfoMarker(place = place)
                        }
                    )
                }
            }
        }

        if(placeBoxShow){
            Button(
                onClick = {
                    setMiddlePlaceToList()
                    placeBoxShow = false
                          },
                modifier = Modifier
                    .zIndex(2F)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
                    .shadow(elevation = 5.dp)
            ) {
                Text(text = "Add")
            }
        }
    }
}

@Composable
fun ScheduleShow(
    startLocation: String,
    endLocation: String,
    locationsList: List<Place>,
    isStartLocationChange: (String) -> Unit,
    isEndLocationChange: (String) -> Unit,
    isSavePressed: () -> Unit,
    isStartButtonPressed: () -> Unit,
    isEndButtonPressed: () -> Unit,
    isSaveButtonPressed: () -> Unit,
    goToMemory:() -> Unit
){

    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .background(Color(0xFFFFFFFF))
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Schedule",
                modifier = Modifier
                    .padding(bottom = 5.dp))
        }

        item {
            ScheduleCardStartEnd(
                location = startLocation,
                isStart = true,
                isValueChange = {
                    isStartLocationChange(it)
                },
                onButtonClick = isStartButtonPressed)
        }

        val show = locationsList.isEmpty()
        Log.d("ShowLocations", show.toString())

        items(locationsList) { location ->
                ScheduleCard(location = location)
            }

        item {
            ScheduleCardStartEnd(
                location = endLocation,
                isStart = false,
                isValueChange = {
                    isEndLocationChange(it)
                },
                onButtonClick = isEndButtonPressed)
        }

        item {
            SelectButton(
                isButtonPressed = {
                    isSaveButtonPressed()
                    goToMemory()
                                  },
                text = "Add",
                modifier = Modifier
                    .padding(top = 5.dp))
        }
    }
}

@Composable
fun ScheduleCard(
    location: Place
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Stop",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center)
        Image(
            painter = painterResource(R.drawable.nav_middle),
            contentDescription = "nav_middle",
            modifier = Modifier
                .weight(1f))
        Text(
            text = location.placeName!!,
            modifier = Modifier
                .weight(3f))

    }
}

@Composable
fun ScheduleCardExample(
    location: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Stop",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center)
        Image(
            painter = painterResource(R.drawable.nav_middle),
            contentDescription = "nav_middle",
            modifier = Modifier
                .weight(1f))
        Text(
            text = location,
            modifier = Modifier
                .weight(3f))

    }
}

@Composable
fun ScheduleCardStartEnd(
    location: String,
    isStart: Boolean,
    isValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val focusManager = LocalFocusManager.current
        Text(
            text = if (isStart) {
                "Start"
            }else{
                 "End"
                 },
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center)
        Image(
            painter = if(isStart){
                painterResource(R.drawable.nav_start)
            }else{
                painterResource(R.drawable.nav_end)
            },
            contentDescription = "nav_start",
            modifier = Modifier
                .weight(1f)
                .height(58.dp))
        TextField(
            value = location,
            singleLine = true,
            onValueChange = {
                location -> isValueChange(location)
            },
            label = {
                Text(
                    text = if(isStart){
                        "Start location"
                    }else{
                        "Destination"
                    },
                    style = TextStyle(fontSize = 12.sp),
                )},
            modifier = Modifier
                .weight(3f),
            textStyle = TextStyle(fontSize = 12.sp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onButtonClick()
                }
            ),)
    }
}

@Composable
fun LocationButtons(
    modifier: Modifier,
    onLocationTypeChanged:(Int) -> Unit
){
    Column(modifier = modifier) {
        MapBtnNames.mapButtons.forEach{
            mapButton -> LocationButton(
            buttonDetail = mapButton,
                onLocationButtonPressed = {
                    onLocationTypeChanged(mapButton.btnValue)
                })
        }
    }
}

@Composable
fun LocationButton(
    onLocationButtonPressed: () -> Unit,
    buttonDetail: MapButton
){
    ElevatedButton(
        onClick = { onLocationButtonPressed() },
        modifier = Modifier
            .padding(5.dp)
            .width(40.dp)
            .height(40.dp),
        contentPadding = PaddingValues(5.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Image(painter = painterResource(buttonDetail.buttonImage),
            contentDescription = stringResource(buttonDetail.buttonNameId),
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun cardInfoMarker(
    place: Place,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .width(250.dp)
            .height(100.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.marker_image),
                contentDescription = place.placeName,
                modifier = Modifier
                    .weight(1F)
            )
            
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(2F)
            ) {
                Text(
                    text = place.placeName!!,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 20.dp),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

fun getStartLocation(current: Context, location: String): LatLng{
    val geocoder= Geocoder(current, Locale.getDefault())

    try {
        val addresses = geocoder.getFromLocationName(location, 1)

        if (addresses != null) {
            if (addresses.isNotEmpty()) {

                return LatLng(
                    addresses[0].latitude,
                    addresses[0].longitude)
            }
        }
        else{
            return LatLng(6.927079, 79.861244)
        }
    }catch (e: IOException) {
        e.printStackTrace()
    }
    return LatLng(6.927079, 79.861244)
}

private fun getDirections(current: Context,origin: String, destination: String, onResult: (List<LatLng>?) -> Unit){
    val apiKey = current.getString(R.string.google_maps_api_key)

    val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val urlBuilder = HttpUrl.Builder()
        .scheme("https")
        .host("maps.googleapis.com")
        .addPathSegment("maps")
        .addPathSegment("api")
        .addPathSegment("directions")
        .addPathSegment("json")
        .addQueryParameter("origin", origin)
        .addQueryParameter("destination", destination)
        .addQueryParameter("key", apiKey)

    val request = Request.Builder()
        .url(urlBuilder.build())
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            withContext(Dispatchers.Main) {
                println("ResponseBody: $responseBody")
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

}

private fun getDirectionsPoly(key: String, origin: LatLng, destination: LatLng,
                          onPathReady: (List<LatLng>) -> Unit){

    val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val json = "application/json; charset=utf-8".toMediaTypeOrNull()

    val routeDetails = """
        {
            "origin": {
                "location": {
                    "latLng": {
                        "latitude": ${origin.latitude},
                        "longitude": ${origin.longitude}
                    }
                }
            },
            "destination": {
                "location": {
                    "latLng": {
                        "latitude": ${destination.latitude},
                        "longitude": ${destination.longitude}
                    }
                }
            },
            "travelMode": "DRIVE",
        }
    """.trimIndent()

    val body = routeDetails.toRequestBody(json)

    val request = Request.Builder()
        .url("https://routes.googleapis.com/directions/v2:computeRoutes")
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("X-Goog-Api-Key", key)
        .addHeader("X-Goog-FieldMask",
            "routes.polyline.encodedPolyline")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val jsonResponse = JSONObject(responseBody)
            val routes = jsonResponse.getJSONArray("routes")
            val route = routes.getJSONObject(0)
            val polyline = route.getJSONObject("polyline")
            val polylineCode = polyline.getString("encodedPolyline")

            val decodedPath = PolyUtil.decode(polylineCode)

            withContext(Dispatchers.Main) {
                onPathReady(decodedPath)
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

}

private fun fetchPlacesNearPolyline(key: String,
                                    middleLocation: LatLng,
                                    radius: Int,
                                    onLocationsReady: (List<Place>) -> Unit){

    val places = mutableListOf<Place>()

    val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val json = "application/json; charset=utf-8".toMediaTypeOrNull()

    val routeDetails = """
        {
          "includedTypes": ["historical_landmark","national_park","tourist_attraction","church","hindu_temple","mosque","synagogue","restaurant","cafe","hotel","guest_house"],
          "maxResultCount": 20,
          "locationRestriction": {
            "circle": {
              "center": {
                "latitude": ${middleLocation.latitude},
                "longitude": ${middleLocation.longitude}},
              "radius": $radius
            }
          }
        }
    """.trimIndent()

    val body = routeDetails.toRequestBody(json)

    val request = Request.Builder()
        .url("https://places.googleapis.com/v1/places:searchNearby")
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("X-Goog-Api-Key", key)
        .addHeader("X-Goog-FieldMask",
            "places.displayName,places.types,places.id,places.location")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            println(responseBody)

            val jsonResponse = JSONObject(responseBody)
            val locTypes = jsonResponse.getJSONArray("places")

            for ( i in 0 until locTypes.length() ) {

                val typesList = mutableListOf<String>()

                val placeObject = locTypes.getJSONObject(i)
                val displayNameObject = placeObject.getJSONObject("displayName")
                val placeName = displayNameObject.getString("text")

                val placeId = placeObject.getString("id")

                val placeTypes = placeObject.getJSONArray("types")

                val placeLocationObject = placeObject.getJSONObject("location")
                val placeLatObject = placeLocationObject.getDouble("latitude")
                val placeLongObject = placeLocationObject.getDouble("longitude")
                val placeLocation = LatLng(placeLatObject, placeLongObject)


                for ( i in 0 until placeTypes.length() ){
                    val placeTypeText = placeTypes.getString(i)

                    typesList.add(placeTypeText)
                }

                var place = Place(placeName, placeId, typesList, placeLocation)
                println(place.placeName)

                places.add(place)
            }

            withContext(Dispatchers.Main) {
                onLocationsReady(places)
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
    }
}

private fun fetchPlacesNearPolylineBak(key: String,
                                    selectedLocationType: Int,
                                    middleLocation: LatLng){

    val places = mutableListOf<String>()
    
    val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val json = "application/json; charset=utf-8".toMediaTypeOrNull()

    val includeLocationType =
        when(selectedLocationType){
            1 -> listOf<String>("historical_landmark","national_park","tourist_attraction")
            2 -> listOf<String>("church","hindu_temple","mosque","synagogue")
            3 -> listOf<String>("restaurant","cafe")
            4 -> listOf<String>("hotel","guest_house")
            else -> listOf<String>("tourist_attraction")
        }

    val routeDetails = """
        {
          "includedTypes": $includeLocationType,
          "maxResultCount": 10,
          "locationRestriction": {
            "circle": {
              "center": {
                "latitude": ${middleLocation.latitude},
                "longitude": ${middleLocation.longitude}},
              "radius": 1000.0
            }
          }
        }
    """.trimIndent()

    val body = routeDetails.toRequestBody(json)

    val request = Request.Builder()
        .url("https://places.googleapis.com/v1/places:searchNearby")
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("X-Goog-Api-Key", key)
        .addHeader("X-Goog-FieldMask",
            "places.displayName,places.types")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            println(responseBody)

            val jsonResponse = JSONObject(responseBody)
            val locTypes = jsonResponse.getJSONArray("places")

            for ( i in 0 until locTypes.length() ) {

                val typesList = mutableListOf<String>()

                val placeObject = locTypes.getJSONObject(i)
                val displayNameObject = placeObject.getJSONObject("displayName")
                val placeName = displayNameObject.getString("text")

            }

            val displayNameObject: JSONObject = jsonResponse.getJSONObject("displayName")
            val locationText: String = displayNameObject.getString("text")

//            withContext(Dispatchers.Main) {
//                onPathReady(decodedPath)
//            }
        } catch (e: IOException){
            e.printStackTrace()
        }
    }
}
