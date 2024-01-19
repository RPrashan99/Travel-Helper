package com.example.travelproject_1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelproject_1.data.AuthRepository
import com.example.travelproject_1.data.LoginState
import com.example.travelproject_1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    val _signInState = Channel<LoginState>()
    val signInState = _signInState.receiveAsFlow()


    fun loginUser(email:String, password:String) = viewModelScope.launch{
        repository.loginUser(email,password).collect{result ->
            when(result){
                is Resource.Success ->{
                    _signInState.send(LoginState(isSuccess = "Sign in success"))
                }
                is Resource.Loading ->{
                    _signInState.send(LoginState(isLoading = true))
                }
                is Resource.Error -> {
                    _signInState.send(LoginState(isError = result.message))
                }
            }
        }
    }
}