package com.example.travelproject_1.data

import com.google.firebase.firestore.GeoPoint

data class PlaceFire(
    val placeName: String?,
    val placeID: String?,
    val placeType: List<String>,
    val placeLocation: GeoPoint?
) {
    constructor() : this(
        null,
        null,
        placeType = listOf(),
        placeLocation = null
    )
}
