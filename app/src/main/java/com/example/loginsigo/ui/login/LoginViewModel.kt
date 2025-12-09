package com.example.loginsigo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginsigo.data.AuthRepository
import com.example.loginsigo.data.model.LoginRequest
import com.example.loginsigo.data.model.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: UserResponse? = null
)

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // Estado mutable interno que el ViewModel puede modificar
    private val _uiState = MutableStateFlow(LoginUiState())
    // Estado inmutable que se expone a la UI (View) para ser observado
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername, errorMessage = null)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword, errorMessage = null)
    }

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

    fun clearFields() {
        _uiState.value = LoginUiState(errorMessage = null) // Restablece todo el estado (limpia campos y mensajes)
    }
}