package com.example.loginsigo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginsigo.data.AuthRepository
import com.example.loginsigo.data.model.LoginRequest
import com.example.loginsigo.data.model.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la interfaz de usuario para la pantalla de inicio de sesión.
 *
 * @param username El nombre de usuario actual.
 * @param password La contraseña actual.
 * @param isLoading `true` si hay una operación de inicio de sesión en curso.
 * @param loginSuccess `true` si el inicio de sesión fue exitoso.
 * @param errorMessage Mensaje de error a mostrar, o `null` si no hay error.
 * @param user La información del usuario si el inicio de sesión fue exitoso.
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: UserResponse? = null
)

/**
 * ViewModel para la pantalla de inicio de sesión.
 *
 * Gestiona el estado de la UI (`LoginUiState`) y maneja la lógica de negocio
 * para la autenticación del usuario a través del [AuthRepository].
 *
 * @param repository El repositorio para manejar la lógica de autenticación.
 */
class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // Estado mutable interno que el ViewModel puede modificar
    private val _uiState = MutableStateFlow(LoginUiState())
    /**
     * Estado inmutable de la UI que se expone a la Vista para ser observado.
     */
    val uiState: StateFlow<LoginUiState> = _uiState

    /**
     * Actualiza el nombre de usuario en el estado de la UI.
     *
     * @param newUsername El nuevo nombre de usuario introducido.
     */
    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername, errorMessage = null)
    }

    /**
     * Actualiza la contraseña en el estado de la UI.
     *
     * @param newPassword La nueva contraseña introducida.
     */
    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword, errorMessage = null)
    }

    /**
     * Inicia el proceso de inicio de sesión.
     *
     * Valida las entradas, actualiza el estado para mostrar la carga, y llama al
     * repositorio para autenticar al usuario. El resultado se refleja en el [uiState].
     */
    fun login() {
        // Validación básica
        if (_uiState.value.username.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "El usuario y la contraseña no pueden estar vacíos.")
            return
        }

        // Bloquear UI e iniciar carga
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, loginSuccess = false)

        viewModelScope.launch {
            val request = LoginRequest(
                username = _uiState.value.username,
                password = _uiState.value.password
            )

            val result = repository.login(request)

            _uiState.value = result.fold(
                onSuccess = { userResponse ->
                    // Login exitoso
                    _uiState.value.copy(
                        isLoading = false,
                        loginSuccess = true,
                        user = userResponse,
                        errorMessage = null
                    )
                },
                onFailure = { throwable ->
                    // Login fallido
                    _uiState.value.copy(
                        isLoading = false,
                        loginSuccess = false,
                        errorMessage = throwable.message ?: "Error desconocido en la conexión."
                    )
                }
            )
        }
    }

    /**
     * Restablece el estado de la UI a sus valores iniciales, limpiando los campos
     * y cualquier mensaje de error.
     */
    fun clearFields() {
        _uiState.value = LoginUiState(errorMessage = null)
    }
}
