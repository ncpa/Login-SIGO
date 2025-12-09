package com.example.loginsigo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val termsConditions: Boolean,
    val registerUser: String,
    val active: Boolean,
    val messageControl: String,
    val accessModule: String,
    val personFullName: String,
    val personId: Int,
    val register: String,
    val profileName: String,
    val email: String,
    val id: Int,
    val username: String,
    val password: String, // Aunque no se recomienda devolver la contraseña
    val roles: List<String>,
    val bearer: String
)
