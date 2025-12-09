package com.example.loginsigo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginsigo.data.AuthRepository
import com.example.loginsigo.ui.login.LoginViewModel

class LoginViewModelFactory(
    // 1. Recibe la dependencia que el ViewModel necesita
    private val repository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 2. Verifica si el ViewModel solicitado es LoginViewModel
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            // 3. Crea la instancia inyectando el repositorio
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}