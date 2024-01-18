package com.example.travelproject_1.data

import com.google.android.gms.maps.model.LatLng

data class FinderUiState(
    val searchLocation: String = "",
    val originLocation: LatLng = LatLng(6.927079, 79.861244 ),
    val foundLocations: List<Place> = listOf(),
    val isFoundLocationsShow: Boolean = false
)