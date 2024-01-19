package com.example.travelproject_1.ui
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelproject_1.R
import com.example.travelproject_1.components.CAccountRow
import com.example.travelproject_1.components.CButton
import com.example.travelproject_1.components.CTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    registerViewModel: RegistrationViewModel = hiltViewModel(),
    onSignInButtonTap: () -> Unit,
    onRegisterButtonTap: () -> Unit,
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    var scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = registerViewModel.signUpState.collectAsState(initial = null)

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {


        Box(modifier = Modifier.fillMaxSize())
        {
            Column(modifier = Modifier) {


                Box(modifier = Modifier.fillMaxSize()
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.sunny_beach),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }

            }
            Column {
                Text(
                    text = "Hello, don't you have an account?",
                    style = TextStyle(
                        fontWeight = FontWeight(600),
                        fontSize = 16.sp,
                        color = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        //.align(Alignment.Top)
                        .padding(top = 20.dp, start = 20.dp, bottom = 5.dp)

                )
                Text(
                    text = "Sign Up",
                    style = TextStyle(
                        fontWeight = FontWeight(600),
                        fontSize = 36.sp,
                        color = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        .padding(top =5.dp , start = 30.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 110.dp)
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )

            ) {

                Image(
                    painter = painterResource(R.drawable.sign_up),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 5.dp)
                        .height(100.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Column(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                        .align(Alignment.CenterHorizontally)

                ) {
                    CTextField(label = "User Name", placeholder = "Enter Email", value = email, onValueChange = { email = it})
                    CTextField(label = "Password" ,placeholder = "Enter Password", value = password, onValueChange = {password = it})
                    CButton(text = "Sign Up", onClick = {
                        scope.launch {
                            registerViewModel.registerUser(email, password)
                        }
                    })
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (state.value?.isLoading == true) {
                            CircularProgressIndicator()
                        }
                    }
                    CAccountRow(text1 = "Already have an account ? ", text2 = "Sign In " ,
                        onText2Tap = { onSignInButtonTap()})

                    LaunchedEffect(key1 = state.value?.isSuccess){
                        scope.launch {
                            if(state.value?.isSuccess?.isNotEmpty() == true){
                                val success = state.value?.isSuccess
                                Toast.makeText(context,"${success}",Toast.LENGTH_LONG).show()

                                delay(1000)

                                onRegisterButtonTap()
                            }
                        }
                    }
                    LaunchedEffect(key1 = state.value?.isError){
                        scope.launch {
                            if(state.value?.isError?.isNotEmpty() == true){
                                val error = state.value?.isError
                                Toast.makeText(context,"${error}",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true,widthDp = 320, heightDp = 640)
@Composable
fun RegistrationPrev() {
    RegistrationScreen( onSignInButtonTap = {}, onRegisterButtonTap = {})
}

