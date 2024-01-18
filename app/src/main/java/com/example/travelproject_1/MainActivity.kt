package com.example.travelproject_1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.travelproject_1.ui.theme.TravelProject_1Theme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequired: Boolean = false

    override fun onResume() {
        super.onResume()
        if(locationRequired){
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiKey = BuildConfig.MAPS_API_KEY
        Log.d(TAG, "onCreate Called")
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TravelProject_1Theme {
                TravelHelperApp()
            }
        }
    }
}


sealed class DestinationScreen(val route:String){
    object Start:DestinationScreen("start")
    object  Signup:DestinationScreen("registration")
    object  SignIn:DestinationScreen("login")
}

