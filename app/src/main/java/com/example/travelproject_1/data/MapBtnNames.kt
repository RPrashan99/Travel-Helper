package com.example.travelproject_1.data

import com.example.travelproject_1.R
import com.example.travelproject_1.model.MapButton

object MapBtnNames {
    val mapButtons= listOf<MapButton>(
        MapButton(1,R.string.natural, R.drawable.natural),
        MapButton(2,R.string.worship, R.drawable.worship),
        MapButton(3,R.string.food, R.drawable.food),
        MapButton(4,R.string.hotel, R.drawable.hotel)
    )
}