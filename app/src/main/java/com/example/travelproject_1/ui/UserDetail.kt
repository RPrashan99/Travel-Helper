package com.example.travelproject_1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelproject_1.R
import com.example.travelproject_1.data.SessionManager

@Preview
@Composable
fun UserScreen(){

    val userEmail = SessionManager.currentUser?.email!!
    
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp),
        ) {

            Text(
                text = "User Details",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.Center)
                    .background(Color(R.color.primaryColor))
            )
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(colorResource(R.color.primaryColor),
                    shape = RoundedCornerShape(100)
                ) // Background color for circle
        ) {
            Image(
                painter = painterResource(R.drawable.user_icon),
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "User Email",
            modifier = Modifier,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )

        Text(
            text = userEmail,
            modifier = Modifier,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp
            )
        )

    }
}