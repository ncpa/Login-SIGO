package com.example.loginsigo

import android.app.Application
import com.example.loginsigo.di.AppContainer

class SigoLoginApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        // Inicializar el contenedor con el contexto de la aplicación
        container = AppContainer(applicationContext)
    }
}