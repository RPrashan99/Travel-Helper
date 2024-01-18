package com.example.travelproject_1.ui

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelproject_1.BuildConfig
import com.example.travelproject_1.components.SearchTextBox
import com.example.travelproject_1.data.Place
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import java.io.IOException
import java.util.Locale

@Preview
@Composable
fun FinderScreen(
    finderViewModel: FinderViewModel = viewModel()
){
    val uiState by finderViewModel.uiState.collectAsState()
    val key = BuildConfig.MAPS_API_KEY
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){

        SearchTextBox(
            searchLocation = finderViewModel.searchedLocation,
            onSearchLocationChanged = {
                finderViewModel.setLocationSearched(it)
            },
            onSearchBtnClicked = {
                val locationLatLong = getLocation(context, finderViewModel.searchedLocation)
                finderViewModel.findLocationPressed(locationLatLong)
            },
            modifier = Modifier
                .width(280.dp)
                .height(80.dp)
                .align(Alignment.TopCenter)
                .padding(top = 30.dp)
                .zIndex(2f),
        )

        GoogleMap(
            modifier = Modifier
                .zIndex(1f),
            cameraPositionState = CameraPositionState(
                CameraPosition.fromLatLngZoom(
                    uiState.originLocation, 12f
                )
            ),
            onMapLoaded = {
                finderViewModel.checkLocationsFound()
            }
        ) {

            Marker(
                state = MarkerState(position = uiState.originLocation),
                title = "Origin",
                snippet = "You are here"
            )

            if(finderViewModel.foundLocations.isNotEmpty()){
                finderViewModel.foundLocations.forEach{
                    place ->
                        Marker(
                            state = MarkerState(place.placeLocation!!),
                            title = place.placeName
                        )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .zIndex(2f)
                .height(120.dp)
                .background(color = Color.White.copy(alpha = 0.7f)),){

            Text(
                text = "Nearby Locations",
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopStart))

            if(finderViewModel.foundLocations.isEmpty()){
                Text(text = "Nothing to show",
                    modifier = Modifier
                        .align(Alignment.Center))
            }else{
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.BottomStart)
                ) {
                    items(finderViewModel.foundLocations) { location ->
                        LocationCard(location = location)
                    }
                }
            }
        }

//        if(uiState.isFoundLocationsShow){
//            Box(modifier = Modifier
//                .align(Alignment.BottomStart)
//                .zIndex(3f)){
//                FinderLocationList(locations = finderViewModel.foundLocations)
//            }
//        }

    }
}

@Composable
fun FinderLocationList(
    locations: List<Place>
){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

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
fun LocationCard(location: Place){
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(100.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray)
    ) {
            Text(
                text = location.placeName!!,
                modifier = Modifier.padding(5.dp),
                color = Color.White
            )

            Text(
                text = location.placeType.joinToString(
                    separator = " #"
                ){it},
                modifier = Modifier
                    .padding(horizontal = 5.dp)

            )
    }
}

fun getLocation(current: Context, location: String): LatLng {
    val geocoder= Geocoder(current, Locale.getDefault())

    try {
        val addresses = geocoder.getFromLocationName(location, 1)

        if (addresses != null) {
            if (addresses.isNotEmpty()) {

                return LatLng(
                    addresses[0].latitude,
                    addresses[0].longitude)
            }
        }
        else{
            return LatLng(6.927079, 79.861244)
        }
    }catch (e: IOException) {
        e.printStackTrace()
    }
    return LatLng(6.927079, 79.861244)
}