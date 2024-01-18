package com.example.travelproject_1.ui

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelproject_1.R
import com.example.travelproject_1.data.Place
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit


@Preview
@Composable
fun TestScreen(
    testViewModel: TestViewModel = viewModel()
){

    val uiState by testViewModel.uiState.collectAsState()
}

@Composable
fun MapViewExample(
    locationValue: String = "",
    locationFind: String = "",
    locationSearch: (String) -> Unit,
    locationShow: () -> Unit
){

    var locationText by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val locationPosition = getLocation(locationFind, context)
    val cameraPositionState = CameraPositionState(
        position = CameraPosition.fromLatLngZoom(locationPosition,10f))

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            horizontalArrangement = Arrangement.Center
        ){
            TextField(value = locationValue,
                onValueChange = {
                        locationValue -> locationSearch(locationValue)
                },
                modifier = Modifier)

            Button(
                onClick = { locationShow() },
                modifier = Modifier) {
                Text(
                    text = "Search"
                )
            }
        }

        GoogleMap(
            modifier = Modifier.height(500.dp),
            cameraPositionState = cameraPositionState,

        ) {

            Marker(
                state = MarkerState(position = locationPosition),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }
}

private fun getLocation(location: String, current: Context): LatLng{

    val geocoder= Geocoder(current, Locale.getDefault())

    try {
        val addresses = geocoder.getFromLocationName(location, 1)

        if (addresses != null) {
            if (addresses.isNotEmpty()) {

                return LatLng(
                    addresses[0].latitude,
                    addresses[0].longitude
                )
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

private fun getDirections(current: Context,origin: LatLng, destination: LatLng,
                          onPathReady: (List<LatLng>) -> Unit){
    val apiKey = current.getString(R.string.google_maps_api_key)

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
        .addHeader("X-Goog-Api-Key", apiKey)
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
                                    selectedLocationType: Int,
                                    middleLocation: LatLng,
                                    onLocationsReady: (List<Place>) -> Unit){

    val places = mutableListOf<Place>()

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

    val typesArray = JSONArray()
    for (i in includeLocationType){
        typesArray.put(i)
    }
    val typesArrayToString = typesArray.toString()

    val routeDetails = """
        {
          "includedTypes": $typesArrayToString,
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
            "places.displayName,places.types,places.id,places.location")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            //println(responseBody)

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

                places.add(Place(placeName, placeId, typesList, placeLocation))
            }

            withContext(Dispatchers.Main) {
               onLocationsReady(places)
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
    }
}

private fun getMiddleLocation(start: LatLng, end:LatLng): LatLng{
    val middleLat = (start.latitude + end.latitude) / 2
    val middleLng = (start.longitude + end.longitude) / 2
    return LatLng(middleLat, middleLng)
}


