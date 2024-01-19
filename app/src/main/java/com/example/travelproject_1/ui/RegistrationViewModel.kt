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
class RegistrationViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    val _signUpState = Channel<LoginState>()
    val signUpState = _signUpState.receiveAsFlow()


    fun registerUser(email:String, password:String) = viewModelScope.launch{
        repository.registerUser(email,password).collect{result ->
            when(result){
                is Resource.Success ->{
                    _signUpState.send(LoginState(isSuccess = "Registration success"))
                }
                is Resource.Loading ->{
                    _signUpState.send(LoginState(isLoading = true))
                }
                is Resource.Error -> {
                    _signUpState.send(LoginState(isError = result.message))
                }
            }
        }
    }
}