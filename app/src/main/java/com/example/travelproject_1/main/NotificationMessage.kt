package com.example.travelproject_1.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.travelproject_1.FbViewModel

@Composable
fun NotificationMessage(vm: FbViewModel) {
    val notifState = vm.popupNotification.value
    val notifMessage = notifState?.getContentOrNull()
    if (notifMessage != null) {
        Toast.makeText(LocalContext.current,notifMessage,Toast.LENGTH_SHORT).show()
    }

}