package com.example.loginsigo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginsigo.data.AuthRepository
import com.example.loginsigo.ui.login.LoginViewModel

/**
 * Fábrica (Factory) para crear instancias de [LoginViewModel].
 *
 * Esta clase permite la inyección de dependencias en el ViewModel, en este caso,
 * proporcionando el [AuthRepository] que el [LoginViewModel] necesita para funcionar.
 *
 * @param repository El repositorio de autenticación que se inyectará en el ViewModel.
 */
class LoginViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {

    /**
     * Crea una nueva instancia del ViewModel solicitado.
     *
     * @param modelClass La clase del ViewModel que se va a crear.
     * @return Una instancia del ViewModel con sus dependencias.
     * @throws IllegalArgumentException si la clase del ViewModel es desconocida.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
