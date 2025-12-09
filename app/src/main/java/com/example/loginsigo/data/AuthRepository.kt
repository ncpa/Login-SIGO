package com.example.loginsigo.data

import com.example.loginsigo.data.local.TokenManager
import com.example.loginsigo.data.model.LoginRequest
import com.example.loginsigo.data.model.UserResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que gestiona la autenticación del usuario.
 *
 * Esta clase abstrae las fuentes de datos (remota y local) y proporciona una API limpia
 * para realizar operaciones de autenticación como iniciar y cerrar sesión.
 *
 * @param tokenManager El gestor de tokens para almacenar y recuperar el token de sesión.
 */
class AuthRepository(
    private val tokenManager: TokenManager // Inyectamos el TokenManager
) {
    private val apiService = RetrofitClient.apiService

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     *
     * Realiza una llamada a la API y, si tiene éxito, guarda el token de autenticación.
     *
     * @param request El objeto con las credenciales de inicio de sesión.
     * @return Un [Result] que contiene [UserResponse] si el inicio de sesión es exitoso,
     * o una [Exception] si falla.
     */
    suspend fun login(request: LoginRequest): Result<UserResponse> {
        return try {
            val response = apiService.loginUser(request)

            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!

                // *** ACCIÓN CLAVE: GUARDAR EL TOKEN ***
                tokenManager.saveToken(userResponse.bearer)

                // Éxito: código 2xx y cuerpo no nulo
                Result.success(userResponse)
            } else {
                // Fallo: código de error HTTP (4xx, 5xx) o cuerpo nulo
                Result.failure(Exception("Error al iniciar sesión: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            // Excepción de red (sin conexión, timeout, etc.)
            Result.failure(e)
        }
    }

    /**
     * Obtiene el token de autenticación guardado.
     *
     * @return Un [Flow] que emite el token guardado, o `null` si no existe.
     */
    fun getSavedToken(): Flow<String?> = tokenManager.getToken()

    /**
     * Cierra la sesión del usuario eliminando el token de autenticación.
     */
    suspend fun logout() {
        tokenManager.clearToken()
    }
}
