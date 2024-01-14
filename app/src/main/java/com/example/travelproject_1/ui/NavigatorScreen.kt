package com.example.travelproject_1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelproject_1.R

@Preview
@Composable
fun NavigatorScreen(
    navigatorViewModel: NavigatorViewModel = viewModel(),
    location: String = "Location Name"
){

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.map2),
            contentDescription = "Map",
            modifier = Modifier
                .weight(8f),
            contentScale = ContentScale.Crop)

        Text(text = "Next Location")

        NextLocationCard(location)
        
    }

}


@Composable
fun NextLocationCard(
    location: String = "Location",
    locationImage: Int = R.drawable.sunny_beach,
){
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
        ) {
            Image(
                painter = painterResource(locationImage),
                contentDescription = "Location",
                modifier = Modifier
                    .height(100.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp),
            ) {
                Text(text = location,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold)
                )
                Text(text = "Location Type")
                Text(text = "Distance to location")
            }
        }
    }
}