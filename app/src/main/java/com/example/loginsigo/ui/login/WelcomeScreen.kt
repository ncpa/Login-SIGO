package com.example.loginsigo.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loginsigo.data.model.UserResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    user: UserResponse,
    navController: NavController // Recibe el NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Usuario") },
                // Agregamos el botón de navegación/retroceso
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Regresar a Login"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "¡Bienvenido, ${user.personFullName}!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Contenido de los datos (las funciones DataRow se mantienen igual)
            DataRow(label = "Nombre Completo", value = user.personFullName)
            DataRow(label = "Usuario", value = user.username)
            DataRow(label = "Email", value = user.email)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            DataRow(label = "Perfil", value = user.profileName)
            DataRow(label = "Módulo de Acceso", value = user.accessModule)

            Text(
                text = "Roles:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            user.roles.forEach { role ->
                Text(text = " - $role", modifier = Modifier.padding(start = 16.dp))
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            DataRow(label = "ID de Persona", value = user.personId.toString())
            DataRow(label = "ID de Registro", value = user.id.toString())
            DataRow(label = "Usuario Activo", value = if (user.active) "Sí" else "No")
            DataRow(label = "Registro", value = user.register)
            DataRow(label = "Términos y Condiciones", value = if (user.termsConditions) "Aceptados" else "Pendiente")

            // Los campos 'password' y 'bearer' NO se muestran, tal como lo solicitaste.
        }
    }
}

// Composable auxiliar para un formato de fila consistente
@Composable
fun DataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}