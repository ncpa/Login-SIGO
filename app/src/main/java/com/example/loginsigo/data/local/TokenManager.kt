package com.example.loginsigo.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para obtener DataStore a nivel de Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

/**
 * Gestiona el almacenamiento y recuperación del token de autenticación de forma local.
 *
 * Esta clase utiliza [DataStore] de Jetpack para persistir el token de acceso de forma
 * asíncrona y segura.
 *
 * @param context El contexto de la aplicación, necesario para inicializar [DataStore].
 */
class TokenManager(private val context: Context) {

    // Clave para almacenar el token Bearer en DataStore.
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")

    /**
     * Guarda el token de acceso en el DataStore.
     *
     * @param token El token de autenticación a guardar.
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    /**
     * Recupera el token de acceso desde el DataStore.
     *
     * @return Un [Flow] que emite el token de autenticación guardado, o `null` si no existe.
     */
    fun getToken(): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[ACCESS_TOKEN_KEY]
            }
    }

    /**
     * Elimina el token de acceso del DataStore.
     *
     * Usado para funcionalidades como cerrar sesión.
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }
}
