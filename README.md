# Proyecto Login SIGO

Este es un proyecto de ejemplo de una aplicación de inicio de sesión para Android, construido con Kotlin y siguiendo la arquitectura MVVM.

## Características Principales

*   **Inicio de sesión** de usuario contra una API REST.
*   **Persistencia de token** de autenticación para mantener la sesión.
*   **Interfaz de usuario moderna** creada con Jetpack Compose.
*   **Interfaz de Inicio de Sesión:** Pantalla de login para que los usuarios introduzcan sus credenciales.
*   **Comunicación con API REST:** Utiliza Retrofit para comunicarse con un servidor remoto y validar las credenciales del usuario.
*   **Arquitectura Moderna:** Implementa la arquitectura MVVM y utiliza componentes de Android Jetpack.
*   **Manejo de Dependencias:** Configurado con Gradle y el DSL de Kotlin (`build.gradle.kts`) para gestionar las librerías del proyecto.

## Arquitectura y Tecnologías Utilizadas

El proyecto sigue la arquitectura **MVVM (Model-View-ViewModel)**:

*   **Modelo**: Representa los datos y la lógica de negocio. Incluye las clases en `data/model`, el `AuthRepository` y las fuentes de datos (`ApiService` para red y `TokenManager` para datos locales).
*   **Vista**: La interfaz de usuario, construida con **Jetpack Compose**. Las vistas son funciones Composable (`LoginScreen.kt`) que observan los cambios en el ViewModel.
*   **ViewModel**: `LoginViewModel` actúa como intermediario, preparando y gestionando los datos para la Vista.

### Tecnologías y Librerías Principales

*   **Kotlin**: Lenguaje de programación principal.
*   **Jetpack Compose**: Para la construcción de la interfaz de usuario.
*   **Retrofit**: Para la comunicación con la API REST.
*   **ViewModel**: Para gestionar la lógica de la UI y el estado.
*   **DataStore**: Para la persistencia de datos (token de sesión).
*   **Coroutines**: Para manejar operaciones asíncronas.

## Comunicación con API REST: Retrofit

**Retrofit** se utiliza para realizar las llamadas a la API de forma declarativa y sencilla.

1.  **Definición del Servicio**: En `ApiService.kt`, definimos los endpoints de la API.

    ```kotlin
    interface ApiService {
        @POST("ws/rest/auth")
        suspend fun loginUser(
            @Body request: LoginRequest
        ): Response<UserResponse>
    }
    ```

2.  **Cliente Retrofit**: `RetrofitClient.kt` configura y crea una instancia de Retrofit. Especifica la URL base y el conversor de JSON (Gson).

    ```kotlin
    object RetrofitClient {
        private const val BASE_URL = "http://189.206.96.198:8080/"

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
    ```

## Otras Tecnologías Clave

### DataStore

**Preferences DataStore** se usa en `TokenManager.kt` para guardar y recuperar el token de autenticación de forma asíncrona y segura, reemplazando a las SharedPreferences tradicionales.

```kotlin
class TokenManager(private val context: Context) {

    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[ACCESS_TOKEN_KEY]
            }
    }
}
```

## Cómo Empezar

Sigue estos pasos para compilar y ejecutar el proyecto:

1.  Clona el repositorio.
2.  Abre el proyecto en Android Studio.
3.  El IDE se sincronizará con Gradle y descargará las dependencias necesarias.
4.  Ejecuta la aplicación en un emulador o dispositivo físico.

## Dependencias (Gradle con Kotlin DSL)

El archivo `app/build.gradle.kts` gestiona las dependencias del proyecto. Algunas de las más importantes son:

```kotlin
dependencies {
    // Core y UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}
```

## Configuración de Red

Para permitir la comunicación con el servidor a través de HTTP (sin encriptación SSL), se ha configurado una política de seguridad de red.

1.  **`app/src/main/res/xml/network_security_config.xml`**: Este archivo define la configuración que permite el tráfico de texto claro (`cleartextTrafficPermitted="true"`) para el dominio específico de la API.

    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <domain-config cleartextTrafficPermitted="true">
            <domain includeSubdomains="true">189.206.96.198</domain>
        </domain-config>
    </network-security-config>
    ```

2.  **`app/src/main/AndroidManifest.xml`**: El manifiesto de la aplicación se actualiza para usar esta configuración de red mediante el atributo `android:networkSecurityConfig`.

    ```xml
    <application
        ...
        android:networkSecurityConfig="@xml/network_security_config">
        ...
    </application>
    ```
