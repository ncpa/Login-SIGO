package com.example.loginsigo.data.remote

import com.example.loginsigo.data.model.LoginRequest
import com.example.loginsigo.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz que define los endpoints de la API para la autenticación.
 *
 * Utiliza Retrofit para declarar las operaciones de red de forma declarativa.
 */
interface ApiService {
    /**
     * Realiza una petición POST para iniciar sesión.
     *
     * @param request El cuerpo de la petición con las credenciales del usuario.
     * @return Un objeto [Response] de Retrofit que encapsula la respuesta HTTP. Esto permite
     * verificar el código de estado y manejar tanto respuestas exitosas como erróneas.
     * El cuerpo de la respuesta exitosa será un [UserResponse].
     */
    @POST("ws/rest/auth")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<UserResponse> // Usamos Response<T> para manejar el código HTTP
}
