package com.example.loginsigo.data.model

/**
 * Clase de datos que representa el cuerpo de la petición para el inicio de sesión.
 *
 * Esta clase se serializa a JSON para ser enviada a la API.
 *
 * @param username El nombre de usuario.
 * @param password La contraseña del usuario.
 */
data class LoginRequest(
    val username: String,
    val password: String
)
