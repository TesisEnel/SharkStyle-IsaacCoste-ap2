package com.example.sharkstyle.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sharkstyle.presentation.usuario.UsuarioUiState
import com.example.sharkstyle.presentation.usuario.UsuarioViewModel
import com.example.sharkstyle.presentation.login.AuthState
import com.example.sharkstyle.presentation.login.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UsuarioViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    goToHomePage: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("Home")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }
    SignupBodyPage(
        uiState = uiState,
        modifier = modifier,
        goToHomePage = goToHomePage,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignupClick = viewModel::signup,
        onSave = viewModel::saveUsuario
    )
}

@Composable
fun SignupBodyPage(
    uiState: UsuarioUiState,
    modifier: Modifier = Modifier,
    goToHomePage: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignupClick: (String, String, String) -> Unit,
    onSave: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AsyncImage(
                model = "https://sharkstyleimagenes.blob.core.windows.net/sharkstyle/logoapppng.png",
                contentDescription = "SharkStyle",
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SharkStyle",
                fontSize = 25.sp,
                fontWeight = FontWeight.W500
            )
        }

        AsyncImage(
            model = "https://sharkstyleimagenes.blob.core.windows.net/sharkstyle/logoconnombrepng.png",
            contentDescription = "Imagen decorativa",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(300.dp)
                .padding(vertical = 1.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Crear Cuenta", fontSize = 32.sp)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = onNameChange,
                label = { Text(text = "Nombre") },
                isError = uiState.errorNombre.isNotBlank(),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            if (uiState.errorNombre.isNotBlank()) {
                Text(text = uiState.errorNombre, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text(text = "Email") },
                isError = uiState.errorEmail.isNotBlank(),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            if (uiState.errorEmail.isNotBlank()) {
                Text(text = uiState.errorEmail, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text(text = "Contraseña") },
                isError = uiState.errorPassword.isNotBlank(),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            if (uiState.errorPassword.isNotBlank()) {
                Text(text = uiState.errorPassword, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        onSignupClick(uiState.nombre, uiState.email, uiState.password)
                        onSave()
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Crear Cuenta")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { goToHomePage() }) {
                Text(text = "Ya tengo una cuenta", textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else if (uiState.errorMessage.isNotBlank()) {
                Text(
                    text = uiState.errorMessage,
                    fontSize = 14.sp,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
