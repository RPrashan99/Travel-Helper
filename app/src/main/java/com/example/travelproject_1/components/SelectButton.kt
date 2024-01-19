package com.example.travelproject_1.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.travelproject_1.R

@Composable
fun SelectButton(
    isButtonPressed: () -> Unit,
    text: String = "Button",
    modifier: Modifier
){
    Button(
        onClick = { isButtonPressed() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.primaryColor)
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.k2d)),
                fontSize = 15.sp
            )
        )
    }
}