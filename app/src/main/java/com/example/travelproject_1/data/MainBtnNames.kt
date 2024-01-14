package com.example.travelproject_1.data

import com.example.travelproject_1.R
import com.example.travelproject_1.model.MainButton

object MainBtnNames {
    val mainButtons= listOf<MainButton>(
        MainButton(R.string.planner, 0xFF162186,R.drawable.planner),
        MainButton(R.string.finder, 0xFF11646A, R.drawable.finder),
        MainButton(R.string.navigator, 0xFF4D1388, R.drawable.navigator),
        MainButton(R.string.memory, 0xFF0A6F20, R.drawable.memory)
    )
}