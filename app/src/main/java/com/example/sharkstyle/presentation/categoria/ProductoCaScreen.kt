package com.example.sharkstyle.presentation.categoria

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.pruebasharkstyle.R
import com.example.sharkstyle.presentation.carrito.CarritoUiEvent
import com.example.sharkstyle.presentation.producto.ProductoUiState
import com.example.sharkstyle.presentation.producto.ProductoViewModel

@Composable
fun ProductoCaScreen(
    viewModel: ProductoViewModel = hiltViewModel(),
    productoId: Int,
    gotoHome: () -> Unit,
    gotoCart: () -> Unit,
    gotoUser: () -> Unit,
    onBackPressed: () -> Unit,
    onNavigateToCarrito: (CarritoUiEvent) -> Unit
) {
    LaunchedEffect(productoId) {
        viewModel.getProducto(productoId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProductoCaBodyScreen(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onNavigateToCarrito = { event ->
            onNavigateToCarrito(event)
            gotoCart()
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProductoCaBodyScreen(
    uiState: ProductoUiState,
    onBackPressed: () -> Unit,
    onNavigateToCarrito: (CarritoUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier.clickable(onClick = onBackPressed)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.back),
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(45.dp)
                                    .background(Color.White, shape = RoundedCornerShape(50.dp))
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "Search",
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.White, shape = RoundedCornerShape(50.dp))
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    AsyncImage(
                        model = uiState.imagen,
                        contentDescription = "Product Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(
                    text = uiState.titulo ?: "Producto Desconocido",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = uiState.precio.let { "RD$$it" } ?: "Sin precio",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4CAF50),
                    fontSize = 22.sp
                )

                Text(
                    text = uiState.descripcion ?: "Descripción no disponible",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Select Size",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.detalleProducto ?: emptyList()) { size ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                                .clickable { /* Acción al seleccionar talla */ }
                        ) {
//                            Text(text = size, color = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {  },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF000000),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Buy Now")
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color(0xFF000000),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { /* Acción al hacer clic en agregar al carrito */ }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.carritoplus),
                            contentDescription = "Add to Cart",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    )
}