package com.example.travelproject_1.ui

import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelproject_1.data.Place
import com.example.travelproject_1.data.PlaceFire
import com.example.travelproject_1.data.PlanUiState
import com.example.travelproject_1.data.TravelPlan
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class PlannerViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

    var showAlertAlreadyAdded by mutableStateOf(false)

    var enteredStartLocation by mutableStateOf("")
        private set

    var startLocationLatLng by mutableStateOf<LatLng>(
        LatLng(0.0,0.0)
    )
        private set

    var enteredEndLocation by mutableStateOf("")
        private set

    var endLocationLatLng by mutableStateOf<LatLng>(
        LatLng(0.0,0.0)
    )
        private set

    var locationType by mutableStateOf(1)
        private set

    var placesNearBy by mutableStateOf(listOf<Place>())
        private set

    var placesNearByType by mutableStateOf(listOf<Place>())
        private set

    var middleLocationLatLang by mutableStateOf<LatLng>(
        LatLng(0.0, 0.0)
    )
        private set

    var cameraLocationLatLang by mutableStateOf<LatLng>(
        LatLng(0.0, 0.0)
    )
        private set

    var distanceStartToEnd by mutableStateOf(0)
        private set

    var addLocationStandby by mutableStateOf<Place?>(null)
        private set

    var locationsAdded by mutableStateOf(listOf<Place>())
        private set

    var saveSucess by mutableStateOf(false)
        private set

    var saveNotSucess by mutableStateOf(false)
        private set


    fun addStartLocation(location: String){
        enteredStartLocation = location
    }

    fun setStartLocation(latLng: LatLng){
        startLocationLatLng = latLng
        _uiState.update {currentState ->
            currentState.copy(
                startLocation = enteredStartLocation
            )
        }
        Log.d("SetStart","Start Button Pressed")
        setCameraPosition()
        checkStartEnd()
    }

    fun addEndLocation(location: String){
        enteredEndLocation = location
    }

    fun setEndLocation(latLng: LatLng){
        endLocationLatLng = latLng
        _uiState.update {currentState ->
            currentState.copy(
                destination = enteredEndLocation
            )
        }
        setCameraPosition()
        checkStartEnd()
    }

    fun addLocationStandby(location: Place){
        addLocationStandby = location
    }

    fun addLocationToPath(){

        val newLocations: MutableList<Place> = uiState.value.locations

        if(locationsAdded.contains(addLocationStandby)){
            showAlertAlreadyAdded = true
        }else{
            newLocations.add(addLocationStandby!!)

            locationsAdded = locationsAdded+addLocationStandby!!

            Handler(Looper.getMainLooper()).postDelayed({
                _uiState.update {currentState ->
                    currentState.copy(
                        locations = newLocations
                    )
                }
            },2000)
        }
    }

    fun addPath(path: List<LatLng>){

        val bounds = LatLngBounds.builder().apply {
            path.forEach{include(it)}
        }.build()

        _uiState.update { currentState ->
            currentState.copy(
                path = path,
                bounds = bounds
            )
        }
    }

    fun clickLocationType(value: Int){
        locationType = value
        _uiState.update { currentState ->
            currentState.copy(
                middlePlaceShow = true
            )
        }
        Log.d("Fun_clickBtn", "Good")

        middleLocationsPlacesForType(value)
    }

    private fun checkStartEnd(){
        if(_uiState.value.startLocation != "" && _uiState.value.destination != ""){
            _uiState.update { currentState ->
                currentState.copy(
                    pathShow = true
                )
            }

            val location1 = Location("Location 1")
            location1.latitude = startLocationLatLng.latitude
            location1.longitude = startLocationLatLng.longitude

            val location2 = Location("Location 2")
            location2.latitude = endLocationLatLng.latitude
            location2.longitude = endLocationLatLng.longitude

            val distance = location1.distanceTo(location2)
            distanceStartToEnd = distance.toInt()/2

            getMiddleLocation()
        }
    }

    fun setNearbyPlaces(places: List<Place>){
        placesNearBy = places
    }

    private fun getMiddleLocation(){
        val middleLat = (startLocationLatLng.latitude + endLocationLatLng.latitude) / 2
        val middleLong = (startLocationLatLng.longitude + endLocationLatLng.longitude) / 2

        val middleLatLong = LatLng(
            middleLat, middleLong
        )

        middleLocationLatLang = middleLatLong
        setCameraPosition()
    }

    fun middleLocationsPlacesForType(type: Int){

        val middlePlaces : MutableList<Place> = arrayListOf()

        val locationTypeOne : Collection<String> = arrayListOf("historical_landmark","national_park","tourist_attraction")
        val locationTypeTwo : Collection<String> = arrayListOf("church","hindu_temple","mosque","synagogue")
        val locationTypeThree : Collection<String> = arrayListOf("restaurant","cafe")
        val locationTypeFour : Collection<String> = arrayListOf("hotel","guest_house")

        placesNearBy.forEach {
            place -> if(place.placeType.any{
                it in when(type){
                    1 -> locationTypeOne
                    2 -> locationTypeTwo
                    3 -> locationTypeThree
                    4 -> locationTypeFour
                    else -> locationTypeOne
                }
            }
            ){
                middlePlaces.add(place)
        }
        }

        placesNearByType = middlePlaces

        if(middlePlaces.isNotEmpty()){
            Log.d("Fun_middleType", "Good")
        }else{
            Log.d("Fun_middleType", "Bad")
        }
    }

    private fun setCameraPosition(){
        if(uiState.value.pathShow){
            cameraLocationLatLang = middleLocationLatLang
        }else{
            if(uiState.value.startLocation != ""){
                cameraLocationLatLang = startLocationLatLng
            }else{
                cameraLocationLatLang = endLocationLatLng
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

    fun checkAndRunDatabaseInsertion(){
        if(
            uiState.value.startLocation != "" && uiState.value.destination != "" && locationsAdded.isNotEmpty()
        ){
            addPlanToDatabase()
        }else{

        }
    }

    private fun addPlanToDatabase(){

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val dbTravelPlans: CollectionReference = db.collection("TravelPlans")

        var locations: MutableList<PlaceFire> = mutableListOf()

        for(i in locationsAdded){
            val locLat = i.placeLocation.latitude
            val locLong = i.placeLocation.longitude

            val geoPoint = GeoPoint(locLat,locLong)

            val placeFire = PlaceFire(i.placeID,i.placeName,i.placeType,geoPoint)

            locations.add(placeFire)
        }

        val travelPlan =  TravelPlan(
            userId = "Test",
            originName = uiState.value.startLocation,
            originLatLang = GeoPoint(startLocationLatLng.latitude,startLocationLatLng.longitude),
            destinationName = uiState.value.destination,
            destinationLatLang = GeoPoint(endLocationLatLng.latitude,endLocationLatLng.longitude),
            stopLocations = locations
        )

        dbTravelPlans.add(travelPlan).addOnSuccessListener {
            saveSucess = true
            Log.d("Database","Success")
        }.addOnFailureListener {
            saveNotSucess = true
            Log.d("Database","failed")
        }
    }

    private fun againSavePlan(){
        saveNotSucess = false
    }


//    fun getStartLocation(current: Context){
//        val geocoder= Geocoder(current, Locale.getDefault())
//
//        try {
//            val addresses = geocoder.getFromLocationName(enteredStartLocation, 1)
//
//            if (addresses != null) {
//                if (addresses.isNotEmpty()) {
//
//                    startLocationLatLng = LatLng(
//                        addresses[0].latitude,
//                        addresses[0].longitude)
//                }
//            }
//            else{
//                startLocationLatLng = LatLng(6.927079, 79.861244)
//            }
//        }catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    fun getEndLocation(current: Context){
//        val geocoder= Geocoder(current, Locale.getDefault())
//
//        try {
//            val addresses = geocoder.getFromLocationName(enteredEndLocation, 1)
//
//            if (addresses != null) {
//                if (addresses.isNotEmpty()) {
//
//                    endLocationLatLng = LatLng(
//                        addresses[0].latitude,
//                        addresses[0].longitude)
//                }
//            }
//            else{
//                endLocationLatLng = LatLng(6.927079, 79.861244)
//            }
//        }catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
}