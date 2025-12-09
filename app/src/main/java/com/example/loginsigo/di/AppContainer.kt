package com.example.loginsigo.di

import android.content.Context
import com.example.loginsigo.data.AuthRepository
import com.example.loginsigo.data.local.TokenManager

class AppContainer(appContext: Context) {

    // 1. Inicializar TokenManager
    val tokenManager = TokenManager(appContext)

    // Instancia del Repositorio que será inyectada en el ViewModel.
    // Usamos lazy para asegurar que se cree solo cuando se necesite.
    // 2. Inicializar Repositorio e inyectar TokenManager
    val authRepository: AuthRepository by lazy {
        AuthRepository(tokenManager) // Pasar el manager al repositorio
    }
}