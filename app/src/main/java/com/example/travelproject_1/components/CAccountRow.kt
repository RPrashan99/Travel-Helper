package com.example.travelproject_1.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CAccountRow(
    onText2Tap: () -> Unit = {},
    text1: String,
    text2:String
) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp, start = 20.dp),

        ) {
        Text(text1,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF494747)
            )
        )
        Text(text2,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight(800),
                color = Color(0xFF494747),
            ),
            modifier = Modifier
                .clickable {
                    onText2Tap()
                }
        )
    }
}