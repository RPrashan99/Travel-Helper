package com.example.travelproject_1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.travelproject_1.ui.theme.TravelProject_1Theme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback

private const val TAG = "MainActivity"

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
        Log.d(TAG, "onCreate Called")
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TravelProject_1Theme {
                TravelHelperApp()
            }
        }
    }
}