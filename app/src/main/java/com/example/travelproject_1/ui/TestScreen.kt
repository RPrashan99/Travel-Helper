package com.example.travelproject_1.ui

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelproject_1.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit


@Preview
@Composable
fun TestScreen(
    testViewModel: TestViewModel = viewModel()
){

    val uiState by testViewModel.uiState.collectAsState()

    val colombo = LatLng(6.927079, 79.861244)
    val mathugama = LatLng(6.521943200000001, 80.11368519999996)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(colombo, 10f)
    }
    val context = LocalContext.current

    getDirections(context, colombo, mathugama){
        decodedPath -> testViewModel.addPath(decodedPath)
    }

    GoogleMap(
        cameraPositionState = cameraPositionState
    ) {
        if(uiState.path.isNotEmpty()){
            Polyline(points = uiState.path)

            val bounds = LatLngBounds.builder().apply {
                uiState.path.forEach{include(it)}
            }.build()

            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,100)
            cameraPositionState.move(cameraUpdate)
        }

        Marker(
            state = MarkerState(colombo)
        )
        Marker(
            state = MarkerState(mathugama)
        )

    }
}

@Composable
fun MapViewExample(
    locationValue: String = "",
    locationFind: String = "",
    locationSearch: (String) -> Unit,
    locationShow: () -> Unit
){

    var locationText by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val locationPosition = getLocation(locationFind, context)
    val cameraPositionState = CameraPositionState(
        position = CameraPosition.fromLatLngZoom(locationPosition,10f))

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            horizontalArrangement = Arrangement.Center
        ){
            TextField(value = locationValue,
                onValueChange = {
                        locationValue -> locationSearch(locationValue)
                },
                modifier = Modifier)

            Button(
                onClick = { locationShow() },
                modifier = Modifier) {
                Text(
                    text = "Search"
                )
            }
        }

        GoogleMap(
            modifier = Modifier.height(500.dp),
            cameraPositionState = cameraPositionState,

        ) {

            Marker(
                state = MarkerState(position = locationPosition),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }
}

private fun getLocation(location: String, current: Context): LatLng{

    val geocoder= Geocoder(current, Locale.getDefault())

    try {
        val addresses = geocoder.getFromLocationName(location, 1)

        if (addresses != null) {
            if (addresses.isNotEmpty()) {

                return LatLng(
                    addresses[0].latitude,
                    addresses[0].longitude
                )
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

private fun getDirections(current: Context,origin: LatLng, destination: LatLng,
                          onPathReady: (List<LatLng>) -> Unit){
    val apiKey = current.getString(R.string.google_maps_api_key)

    val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val json = "application/json; charset=utf-8".toMediaTypeOrNull()

    val routeDetails = """
        {
            "origin": {
                "location": {
                    "latLng": {
                        "latitude": ${origin.latitude},
                        "longitude": ${origin.longitude}
                    }
                }
            },
            "destination": {
                "location": {
                    "latLng": {
                        "latitude": ${destination.latitude},
                        "longitude": ${destination.longitude}
                    }
                }
            },
            "travelMode": "DRIVE",
        }
    """.trimIndent()

    val body = routeDetails.toRequestBody(json)

    val request = Request.Builder()
        .url("https://routes.googleapis.com/directions/v2:computeRoutes")
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("X-Goog-Api-Key", apiKey)
        .addHeader("X-Goog-FieldMask",
            "routes.polyline.encodedPolyline")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val jsonResponse = JSONObject(responseBody)
            val routes = jsonResponse.getJSONArray("routes")
            val route = routes.getJSONObject(0)
            val polyline = route.getJSONObject("polyline")
            val polylineCode = polyline.getString("encodedPolyline")

            val decodedPath = PolyUtil.decode(polylineCode)

            withContext(Dispatchers.Main) {
                onPathReady(decodedPath)
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

}

