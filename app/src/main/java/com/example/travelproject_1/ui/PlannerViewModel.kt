package com.example.travelproject_1.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelproject_1.data.PlanUiState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlannerViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

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

        checkStartEnd()
    }

    fun addLocation(location: String){

        _uiState.update {currentState ->
            currentState.copy(
                locations = _uiState.value.locations + location
            )
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
    }

    private fun checkStartEnd(){
        if(_uiState.value.startLocation != "" && _uiState.value.destination != ""){
            _uiState.update { currentState ->
                currentState.copy(
                    pathShow = true
                )
            }
        }
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