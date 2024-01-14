package com.example.travelproject_1.ui

import androidx.lifecycle.ViewModel
import com.example.travelproject_1.data.FinderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigatorViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(FinderUiState())
    val uiState: StateFlow<FinderUiState> = _uiState.asStateFlow()


}