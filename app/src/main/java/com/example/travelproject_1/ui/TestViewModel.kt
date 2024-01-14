package com.example.travelproject_1.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelproject_1.data.TestUiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestViewModel: ViewModel(
) {
    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    var enteredLocation by mutableStateOf("")
        private set

    fun addStartLocation(location: String){
        enteredLocation = location
    }

    fun setStartLocation(){
        _uiState.update { currentState ->
            currentState.copy(
                startLocation = enteredLocation
            )
        }
    }

    fun addPath(path: List<LatLng>){
        _uiState.update { currentState ->
            currentState.copy(
                path = path
            )
        }
    }

}