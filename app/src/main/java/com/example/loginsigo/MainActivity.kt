package com.example.loginsigo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loginsigo.data.model.UserResponse
import com.example.loginsigo.di.LoginViewModelFactory
import com.example.loginsigo.ui.login.LoginViewModel
import com.example.loginsigo.ui.login.WelcomeScreen
import com.google.gson.Gson

object Routes {
    const val LOGIN = "login_screen"
    const val WELCOME = "welcome_screen/{userJson}" // Argumento para pasar el JSON

    // Función para construir la ruta con el argumento
    fun welcome(userJson: String): String {
        return "welcome_screen/$userJson"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // Envuelve en tu tema
                AppScreenEntry()
            }
        }
    }
}

// Función Composable de nivel superior para obtener la Application y la Factoría.
@Composable
fun AppScreenEntry() {
    // Obtenemos el contexto de la aplicación para inicializar el AppContainer
    val appContext = LocalContext.current.applicationContext

    // 1. Obtenemos la instancia de nuestra clase Application
    // El AppContainer ahora se crea en SigoLoginApplication(appContext)
    val application = appContext as SigoLoginApplication

    // 2. Accedemos al Repositorio a través del contenedor
    // Ya no necesitas obtener el repositorio, el contenedor lo inicializa.
    val authRepository = application.container.authRepository // Ya tiene el tokenManager inyectado.

    // 3. Creamos la Factoría con el Repositorio
    val factory = LoginViewModelFactory(authRepository)

    // 4. Inicializa el controlador de navegación
    val navController = rememberNavController()

    // 5. Define el NavHost para la navegación
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // --- 1. Pantalla de LOGIN ---
        composable(Routes.LOGIN) {
            val viewModel: LoginViewModel = viewModel(factory = factory)
            val uiState by viewModel.uiState.collectAsState()

            // Llama a la pantalla de Login y le pasa una acción de navegación (onNavigateToWelcome)
            LoginScreen(
                viewModel = viewModel,
                onNavigateToWelcome = { userResponse ->
                    // Serializa el objeto UserResponse a JSON
                    val userJson = Gson().toJson(userResponse)

                    // Navega a la pantalla de bienvenida
                    navController.navigate(Routes.welcome(userJson)) {
                        // Opcional: Evita que el usuario regrese a la pantalla de login con el botón 'atrás'
                        // popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        // --- 2. Pantalla de BIENVENIDA ---
        composable(
            route = Routes.WELCOME,
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            // Obtiene el argumento JSON de la navegación
            val userJson = backStackEntry.arguments?.getString("userJson")

            // Deserializa el JSON de nuevo al objeto UserResponse
            val userResponse = Gson().fromJson(userJson, UserResponse::class.java)

            // Asegúrate de que el objeto no sea nulo antes de mostrar la pantalla
            if (userResponse != null) {
                WelcomeScreen(user = userResponse,
                    navController = navController // Pasamos el controlador a la pantalla)
                )
            } else {
                // Manejo de error si la deserialización falla
                Text("Error al cargar los datos del usuario.")
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel, // Ahora lo pasamos como parámetro directo
    onNavigateToWelcome: (UserResponse) -> Unit // NUEVO PARÁMETRO: la acción de navegación
) {
//    // 5. Instanciamos el ViewModel usando la factoría
//    val viewModel: LoginViewModel = viewModel(factory = factory)

    // Observa el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Efecto secundario para mostrar mensajes (errores o éxito)
    if (uiState.errorMessage != null) {
        Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
        // Opcionalmente, podrías limpiar el mensaje de error en el ViewModel después de mostrarlo
    }

    if (uiState.loginSuccess && uiState.user != null) {
        Toast.makeText(context, "¡Login exitoso! Usuario: ${uiState.user?.personFullName}", Toast.LENGTH_LONG).show()
        onNavigateToWelcome(uiState.user!!)
        //viewModel.resetLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de Usuario
        OutlinedTextField(
            value = uiState.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Usuario") },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Campo de Contraseña
        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        }

        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = viewModel::clearFields) { Text("Limpiar") }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = viewModel::login, enabled = !uiState.isLoading) { Text("Iniciar Sesión") }
        }
    }
}