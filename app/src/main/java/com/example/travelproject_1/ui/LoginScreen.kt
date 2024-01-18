package com.example.travelproject_1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelproject_1.R
import com.example.travelproject_1.components.CAccountRow
import com.example.travelproject_1.components.CButton
import com.example.travelproject_1.components.CTextField

@Composable
fun LoginScreen(
    onSignupButtonTap: () -> Unit = {},
) {
    var emty by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var cPasswordVisibility by remember { mutableStateOf(false) }
    var erroeE by remember { mutableStateOf(false) }
    var erroeCP by remember { mutableStateOf(false) }
    var erroeC by remember { mutableStateOf(false) }
    var erroeP by remember { mutableStateOf(false) }
    var pLength by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ){
        Box(modifier = Modifier.fillMaxSize()) {
            ///background Image
//            Image(painter = painterResource(id = R.drawable.sunny_beach),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(190.dp)
//                    .align(Alignment.BottomCenter)
//            )
            Column(modifier = Modifier) {

                Box(
                    modifier = Modifier.fillMaxSize()
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }

            }
            Column {

                Text(
                    text = "sign In",
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        //.align(Alignment.Top)
                        .padding(top = 30.dp, start = 20.dp, bottom = 5.dp)
                )
                Text(
                    "Sign In now to have an amazing experience with travel helper",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        .padding(top = 10.dp, start = 10.dp, bottom = 20.dp, end = 10.dp)
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 150.dp)
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )

            ) {
                Image(
                    painter = painterResource(id = R.drawable.sign_in2),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 5.dp)
                        .height(100.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                        .align(Alignment.CenterHorizontally)

                ) {
                    CTextField(onValueChange = {email =it}, label = "Email Address", placeholder ="Enter Email", value =email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                        )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (erroeE){
                        Text(text = "Enter Email",
                            color = Color.Red,
                            modifier = Modifier
                                .padding(end = 100.dp))

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (erroeE){
                        Text(text = "Enter password",
                            color = Color.Red,
                            modifier = Modifier
                                .padding(end = 100.dp))

                    }
                    if (pLength){
                        Text(text = "Password must be 6 characters",
                            color = Color.Red,
                            modifier = Modifier
                                .padding(end = 100.dp))

                    }
                    CTextField(
                        onValueChange = {password = it},
                        label = "User Name",
                        placeholder = "Enter Password",
                        visualTransformation = PasswordVisualTransformation(),
                        value = password,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go)
                    )

                    CButton(text = "Sign In")
                    CAccountRow(
                        text1 = "Do not have an account ? ", text2 = "Sign Up",
                        onText2Tap = {
                            onSignupButtonTap()
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun LoginScreenPreview() {
        LoginScreen(onSignupButtonTap = {})
}