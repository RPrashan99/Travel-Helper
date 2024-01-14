package com.example.travelproject_1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.maps.android.compose.GoogleMap

@Preview
@Composable
fun FinderScreen(
    finderViewModel: FinderViewModel = viewModel()
){
    val uiState by finderViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        GoogleMap {
            
        }

        if(uiState.foundLocations.isNotEmpty()){
            Box(modifier = Modifier
                .align(Alignment.BottomStart)){
                FinderLocationList(locations = uiState.foundLocations)
            }
        }

    }
}

@Composable
fun FinderLocationList(
    locations: List<String>
){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Locations Found")

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(locations) { location ->
                LocationCard(location = location)
            }
        }
    }
}

@Composable
fun LocationCard(location: String){
    Card(
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray)
    ) {
            Text(
                text = location,
                modifier = Modifier.padding(16.dp),
                color = Color.White
            )
    }
}