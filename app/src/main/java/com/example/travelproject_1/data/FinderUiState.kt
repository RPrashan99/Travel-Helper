package com.example.travelproject_1.data

data class FinderUiState(
    val searchLocation: String = "",
    val foundLocations: List<String> = listOf(),
)