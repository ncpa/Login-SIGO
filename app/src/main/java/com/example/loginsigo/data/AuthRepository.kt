package com.example.loginsigo.data

import com.example.loginsigo.data.local.TokenManager
import com.example.loginsigo.data.model.LoginRequest
import com.example.loginsigo.data.model.UserResponse
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val tokenManager: TokenManager // Inyectamos el TokenManager
) {
    private val apiService = RetrofitClient.apiService

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
                // Podrías parsear response.errorBody() a un mensaje de error si la API lo proporciona
                Result.failure(Exception("Error al iniciar sesión: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            // Excepción de red (sin conexión, timeout, etc.)
            Result.failure(e)
        }
    }
    // Opcional: Función para obtener el token actual
    fun getSavedToken(): Flow<String?> = tokenManager.getToken()

    // Opcional: Función para cerrar sesión
    suspend fun logout() {
        tokenManager.clearToken()
    }
}