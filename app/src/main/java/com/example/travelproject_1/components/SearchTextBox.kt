package com.example.travelproject_1.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.travelproject_1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextBox(
    searchLocation: String = "",
    searchIcon: Int? = 1,
    onSearchLocationChanged: (String) -> Unit = {},
    onSearchBtnClicked: () -> Unit = {},
    modifier: Modifier
){
    Row(
        modifier = modifier
    ) {

        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = searchLocation,
            onValueChange = {
                searchLocation -> onSearchLocationChanged(searchLocation)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "Search")
            },
            trailingIcon = {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .shadow(elevation = 8.dp)
                        .height(50.dp)
                        .width(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.my_location),
                            contentDescription = "MyLocationIcon")
                }
            },
            placeholder = {
                Text(text = "Search location")
            },
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White.copy(alpha = 0.7f)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Handle "Done" button click
                    // This will also handle "Enter" key if it's the last field in the focus chain
                    focusManager.clearFocus()
                    onSearchBtnClicked()
                }
            )
        )
    }
}