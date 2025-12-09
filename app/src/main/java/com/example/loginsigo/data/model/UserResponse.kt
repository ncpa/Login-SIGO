package com.example.loginsigo.data.model

/**
 * Clase de datos que representa la respuesta del servidor tras un inicio de sesión exitoso.
 *
 * Esta clase es poblada por Gson a partir de la respuesta JSON de la API.
 *
 * @param termsConditions Indica si el usuario ha aceptado los términos y condiciones.
 * @param registerUser El usuario que registró a este usuario.
 * @param active Indica si la cuenta de usuario está activa.
 * @param messageControl Un mensaje de control de la API.
 * @param accessModule Módulo al que el usuario tiene acceso.
 * @param personFullName El nombre completo de la persona asociada a la cuenta.
 * @param personId El ID de la persona asociada a la cuenta.
 * @param register La fecha de registro del usuario.
 * @param profileName El nombre del perfil del usuario.
 * @param email El correo electrónico del usuario.
 * @param id El ID del usuario.
 * @param username El nombre de usuario.
 * @param password La contraseña del usuario (no se recomienda que la API devuelva este dato).
 * @param roles La lista de roles asignados al usuario.
 * @param bearer El token de autenticación Bearer.
 */
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
