package com.example.sharkstyle.presentation.usuario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.sharkstyle.presentation.login.AuthViewModel
import com.example.sharkstyle.presentation.usuario.UsuarioUiState
import com.example.sharkstyle.presentation.usuario.UsuarioViewModel

@Composable
fun UsuarioScreen(
    viewModel: UsuarioViewModel = hiltViewModel(),
    autViewModel: AuthViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    userId: Int
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.getUsuarioById(userId)
    }

    UsuarioBodyScreen(
        uiState = uiState,
        onLogout = onLogout,
        signout = autViewModel::signOut
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioBodyScreen(
    uiState: UsuarioUiState,
    onLogout: () -> Unit,
    signout: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo y título
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
            Text(
                text = "Mi Perfil",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Nombre:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text(uiState.nombre, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)

                    Text("Email:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text(uiState.email, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    onLogout()
                    signout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsuarioScreenPreview() {
    UsuarioBodyScreen(
        uiState = UsuarioUiState(
            nombre = "John Doe",
            email = "john.doe@example.com"
        ),
        onLogout = {},
        signout = {}
    )
}
