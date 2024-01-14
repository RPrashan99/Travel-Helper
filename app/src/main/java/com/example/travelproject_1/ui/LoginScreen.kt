package com.example.travelproject_1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelproject_1.R
import com.example.travelproject_1.components.CTextField
import com.example.travelproject_1.ui.theme.alegreyaFontFamily

@Composable
fun LoginScreen() {
    Surface(
        color = Color(345)
    ){
        Box(modifier = Modifier.fillMaxSize()){
            ///background Image
//            Image(painter = painterResource(id = R.drawable.sunny_beach),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(190.dp)
//                    .align(Alignment.BottomCenter)
//            )

            /// Content
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
            ){
                //logo
                Image(painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 54.dp)
                        .height(100.dp)
                )

                Text(text = "sign In",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontFamily = alegreyaFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF0A8E86)
                    ),
                )

                Text("Sign In now to have an amazing experience with travel helper",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = alegreyaFontFamily,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF494747)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 30.dp, start = 5.dp, bottom = 20.dp)
                )

                CTextField(hint = "Email Address", value ="" )
                CTextField(hint = "Password", value = "" )

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors( containerColor = Color(0xFF0A8E86)),


                    ) {
                    Text(
                        text = "Sign In",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = alegreyaFontFamily,
                            fontWeight = FontWeight(400),
                            color = Color(0xFFFFFFFF)
                        )
                    )
                }
                Text("Do not have an account ?",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = alegreyaFontFamily,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF494747)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 10.dp, start = 40.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}