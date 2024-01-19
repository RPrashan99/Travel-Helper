package com.example.travelproject_1.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CButton(
    onClick: ( ) -> Unit = {},
    text: String,

) {
    Button(
        onClick = {
            onClick()
                  },
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(top = 10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A8E86)),
        shape = RoundedCornerShape(5.dp),


        ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF)
            )
        )
    }
}