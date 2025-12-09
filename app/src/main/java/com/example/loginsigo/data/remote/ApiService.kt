package com.example.loginsigo.data.remote

import com.example.loginsigo.data.model.LoginRequest
import com.example.loginsigo.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("ws/rest/auth")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<UserResponse> // Usamos Response<T> para manejar el código HTTP
}