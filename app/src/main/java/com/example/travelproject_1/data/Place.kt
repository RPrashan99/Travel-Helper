package com.example.travelproject_1.data

import com.google.android.gms.maps.model.LatLng

data class Place(
    val placeName: String,
    val placeID: String,
    val placeType: List<String>,
    val placeLocation: LatLng
)
