package com.example.travelproject_1.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelproject_1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CPassword(
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    value: String,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    passwordVisibility: Boolean,
    cPasswordVisibility: Boolean,
    passwordVisualTransformation: Boolean
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {

            Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color(0xFF575555), fontSize = 16.sp)
        },
        placeholder = {
            Text(text = placeholder,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color( 0xFFCDCDCD)
                )
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()){
                val visibilityIcon = if (passwordVisibility){
                    painterResource(id = R.drawable.background)
                }
                else{
                    painterResource(id = R.drawable.sign_up)
                }
                Icon(painter = visibilityIcon,
                    contentDescription = if (passwordVisibility) "Hide Password" else "Show Password",
                    Modifier.clickable {
                         //cPasswordVisibility = !passwordVisibility
                    }
                )
            }
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clip(shape = RoundedCornerShape(5.dp))
            .background(color = Color.White),


        keyboardOptions = KeyboardOptions.Default,
        visualTransformation = visualTransformation,
        singleLine = true,
        maxLines = 1,

        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.DarkGray,
        ),

        )
}