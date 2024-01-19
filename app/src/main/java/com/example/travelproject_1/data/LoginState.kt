package com.example.travelproject_1.data

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess : String? = "",
    val isError: String? = ""
)
