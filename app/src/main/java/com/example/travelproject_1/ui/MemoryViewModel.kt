package com.example.travelproject_1.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelproject_1.data.Memory

class MemoryViewModel: ViewModel() {

    var memoryList by mutableStateOf(listOf<Memory>())
        private set

}