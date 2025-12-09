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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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

/**
 * Define las rutas de navegación de la aplicación.
 */
object Routes {
    const val LOGIN = "login_screen"
    const val WELCOME = "welcome_screen/{userJson}" // Argumento para pasar el JSON

    /**
     * Construye la ruta para la pantalla de bienvenida con el JSON del usuario.
     * @param userJson El objeto [UserResponse] serializado como una cadena JSON.
     * @return La ruta completa para navegar a la pantalla de bienvenida.
     */
    fun welcome(userJson: String): String {
        return "welcome_screen/$userJson"
    }
}

/**
 * Actividad principal de la aplicación.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppScreenEntry()
            }
        }
    }
}

/**
 * Punto de entrada de la UI de la aplicación.
 *
 * Configura el [NavHost] y la inyección de dependencias para los ViewModels.
 */
@Composable
fun AppScreenEntry() {
    val appContext = LocalContext.current.applicationContext
    val application = appContext as SigoLoginApplication
    val authRepository = application.container.authRepository
    val factory = LoginViewModelFactory(authRepository)
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            val viewModel: LoginViewModel = viewModel(factory = factory)
            LoginScreen(
                viewModel = viewModel,
                onNavigateToWelcome = {
                    val userJson = Gson().toJson(it)
                    navController.navigate(Routes.welcome(userJson))
                }
            )
        }
        composable(
            route = Routes.WELCOME,
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson")
            val userResponse = Gson().fromJson(userJson, UserResponse::class.java)
            if (userResponse != null) {
                WelcomeScreen(user = userResponse, navController = navController)
            } else {
                Text("Error al cargar los datos del usuario.")
            }
        }
    }
}

/**
 * Composable que representa la pantalla de inicio de sesión.
 *
 * @param viewModel El ViewModel que gestiona el estado y la lógica de esta pantalla.
 * @param onNavigateToWelcome Callback que se invoca para navegar a la pantalla de bienvenida
 * tras un inicio de sesión exitoso.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToWelcome: (UserResponse) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Efecto para navegar cuando el login es exitoso.
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess && uiState.user != null) {
            Toast.makeText(context, "¡Login exitoso! Usuario: ${uiState.user?.personFullName}", Toast.LENGTH_LONG).show()
            onNavigateToWelcome(uiState.user!!)
        }
    }

    // Efecto para mostrar mensajes de error.
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
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

        OutlinedTextField(
            value = uiState.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Usuario") },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = viewModel::clearFields, enabled = !uiState.isLoading) { Text("Limpiar") }
            Button(onClick = viewModel::login, enabled = !uiState.isLoading) { Text("Iniciar Sesión") }
        }
    }
}
