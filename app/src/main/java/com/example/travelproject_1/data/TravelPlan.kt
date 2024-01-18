package com.example.travelproject_1.data

import com.google.firebase.firestore.GeoPoint

data class TravelPlan(
    var userId: String?,
    var originName: String?,
    var originLatLang: GeoPoint?,
    var destinationName: String?,
    var destinationLatLang: GeoPoint?,
    var stopLocations: List<PlaceFire>
){
    constructor() : this(null,
        null,
        null,
        null,
        null,
        stopLocations = listOf()
    )
}
