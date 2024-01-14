package com.example.travelproject_1.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MainButton(
    @StringRes val buttonNameId: Int,
    val colorCode: Long,
    @DrawableRes val imageResourceId : Int,
)
