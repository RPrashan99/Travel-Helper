package com.example.travelproject_1.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelproject_1.ui.theme.alegreyaFontFamily
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CTextField(
    onValueChange: (String) -> Unit = {},
    hint: String,
    value: String,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = hint,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = alegreyaFontFamily,
                    color = Color(0xFF575555)
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(color = Color.White,shape = MaterialTheme.shapes.medium),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFBEC2C2),
            unfocusedIndicatorColor = Color(0xFFBEC2C2)
        )
    )
}