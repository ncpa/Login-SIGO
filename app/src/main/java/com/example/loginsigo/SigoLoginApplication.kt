package com.example.loginsigo

import android.app.Application
import com.example.loginsigo.di.AppContainer

/**
 * Clase principal de la aplicación que se extiende de [Application].
 *
 * Esta clase se utiliza para inicializar componentes que deben existir durante todo el ciclo de vida
 * de la aplicación, como el contenedor de inyección de dependencias.
 */
class SigoLoginApplication : Application() {
    /**
     * Contenedor de dependencias de la aplicación.
     *
     * Contiene las instancias de los objetos que necesitan ser compartidos a través de la app,
     * como repositorios y gestores de datos.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Inicializar el contenedor con el contexto de la aplicación
        container = AppContainer(applicationContext)
    }
}
