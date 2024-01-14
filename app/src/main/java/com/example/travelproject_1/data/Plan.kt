package com.example.travelproject_1.data

data class Plan(
    val id: String = "",
    val currentLocation: String = "",
    val destination: String = "",
    var locations: List<String>,

)
