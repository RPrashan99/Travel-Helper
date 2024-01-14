package com.example.travelproject_1.data

import com.google.android.gms.maps.model.LatLng

data class TestUiState(
    val startLocation: String = "",
    val endLocation: String = "",
    val path: List<LatLng> = listOf()
)
