package com.example.travelproject_1.data

import com.google.android.gms.maps.model.LatLng

data class Memory(
    val userID: String,
    val planName: String,
    val planOrigin: String,
    val originLatLong: LatLng,
    val planEnd: String,
    val endLatLong: LatLng,
    val planMiddle: List<Place>
)
