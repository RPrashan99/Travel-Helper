package com.example.travelproject_1.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelproject_1.BuildConfig
import com.example.travelproject_1.data.FinderUiState
import com.example.travelproject_1.data.Place
import com.google.android.gms.maps.model.LatLng
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

class FinderViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FinderUiState())
    val uiState: StateFlow<FinderUiState> = _uiState.asStateFlow()

    val key = BuildConfig.MAPS_API_KEY

    var searchedLocation by mutableStateOf("")
        private set

    var foundLocations by mutableStateOf(listOf<Place>())
        private set

    fun setLocationSearched(location: String){
        searchedLocation = location
    }

    fun findLocationPressed(locationLatLong: LatLng){
        _uiState.update {currentState ->
            currentState.copy(
                isFoundLocationsShow = false,
                searchLocation = searchedLocation,
                originLocation = locationLatLong
            )
        }

        getNearByLocations(key){
            if(it.isNotEmpty()){

                if(it.isNotEmpty()) Log.d("PlaceList","Good")
                else Log.d("PlaceList","Bad")

                foundLocations = it
//                _uiState.update {currentState ->
//                    currentState.copy(
//                        foundLocations = it
//                    )
//                }
            }
        }

    }

    fun checkLocationsFound(){
        if (foundLocations.isNotEmpty()){
            _uiState.update {currentState ->
                    currentState.copy(
                        isFoundLocationsShow = true
                    )
                }
        }
    }

    private fun getNearByLocations(key: String, onLocationsReady: (List<Place>) -> Unit){

        val location = uiState.value.originLocation

        val radius = 3000

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
                "latitude": ${location.latitude},
                "longitude": ${location.longitude}},
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
}