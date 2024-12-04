package com.example.sharkstyle.presentation.usuario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sharkstyle.data.local.entities.UsuarioEntity

@Composable
fun UsuarioListScreen(
    viewModel: UsuarioViewModel = hiltViewModel(),
    gotoUsuario: (Int) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getUsuarios()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UsuarioBodyListScreen(
        uiState = uiState,
        gotoUsuario = gotoUsuario,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioBodyListScreen(
    uiState: UsuarioUiState,
    gotoUsuario: (Int) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Usuarios",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                )
            } else if (uiState.errorMessage.isNotEmpty()) {
                // Mostrar mensaje de error
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(uiState.usuarios) { usuario ->
                        UsuarioRow(
                            usuario = usuario,
                            gotoUsuario = gotoUsuario
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UsuarioRow(
    usuario: UsuarioEntity,
    gotoUsuario: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable{ gotoUsuario(usuario.usuarioId) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "ID: ${usuario.usuarioId}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Nombre: ${usuario.nombre}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Email: ${usuario.email}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
