package com.example.travelproject_1.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelproject_1.data.TestUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TestViewModel: ViewModel(
) {
    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    var newLocation by mutableStateOf("")
        private set

    var locations by mutableStateOf(listOf<String>())
        private set

    fun addLocation(location:String){
        newLocation = location
    }

    fun addToList(){
        locations = locations+newLocation
    }

}