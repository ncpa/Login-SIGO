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

class TokenManager(private val context: Context) {

    // Clave para almacenar el token Bearer
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")

    /** Guarda el token Bearer */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    /** Lee el token Bearer. Retorna un Flow con el token o null. */
    fun getToken(): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[ACCESS_TOKEN_KEY]
            }
    }

    /** Elimina el token (para cerrar sesión) */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }
}