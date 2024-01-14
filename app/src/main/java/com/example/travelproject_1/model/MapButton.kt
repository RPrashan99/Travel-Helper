package com.example.travelproject_1.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MapButton(
    val btnValue: Int,
    @StringRes val buttonNameId: Int,
    @DrawableRes val buttonImage: Int
)
