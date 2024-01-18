package com.example.travelproject_1.data

import com.example.travelproject_1.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
interface AuthRepository {
    fun loginUser(email:String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email:String, password: String): Flow<Resource<AuthResult>>
}