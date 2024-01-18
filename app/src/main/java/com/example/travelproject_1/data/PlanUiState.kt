package com.example.travelproject_1.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

data class PlanUiState(
    val startLocation: String = "",
    val destination: String = "",
    val path: List<LatLng> = listOf(),
    val bounds: LatLngBounds = LatLngBounds(LatLng(6.8772, 79.8355), LatLng(6.9500, 79.9622)),
    val locations: MutableList<Place> = arrayListOf<Place>(),
    val pathShow: Boolean = false,
    val middlePlaceShow: Boolean = false,
    val reset: Boolean = false
)
