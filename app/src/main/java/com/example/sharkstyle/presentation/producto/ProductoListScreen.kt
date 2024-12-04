package com.example.sharkstyle.presentation.producto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.pruebasharkstyle.R
import com.example.sharkstyle.data.local.entities.ProductoEntity
import com.example.sharkstyle.presentation.component.NavigationBarWithAnimatedIcons

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProductoListScreen(
    productoViewModel: ProductoViewModel = hiltViewModel(),
    gotoHome: () -> Unit = {},
    gotoCart: () -> Unit = {},
    gotoUser: () -> Unit = {},
    onProductoClick: (Int) -> Unit = {},
    onCategoriaClick: () -> Unit = {}
) {
    val uiState = productoViewModel.uiState.collectAsStateWithLifecycle()

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
                        Image(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.White, shape = RoundedCornerShape(50.dp))
                        )
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Productos",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.white)
                )
            )
        },
        bottomBar = {
            NavigationBarWithAnimatedIcons(
                gotoHome = gotoHome,
                gotoCart = gotoCart,
                gotoUser = gotoUser
            )
        }
    ) { paddingValues ->
        if (uiState.value.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.value.errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.value.errorMessage, color = Color.Red)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(uiState.value.productos) { index, producto ->
                    ProductoCard(
                        index, producto,onProductoClick
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    index: Int,
    producto: ProductoEntity,
    onProductoClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .width(168.dp)
            .clickable { onProductoClick(producto.productoId) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {

            Image(
                painter = rememberAsyncImagePainter(producto.imagen),
                contentDescription = producto.titulo,
                modifier = Modifier
                    .size(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.titulo,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = "RD$${producto.precio}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp, start = 5.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ProductoListScreenPreview() {
    ProductoListScreen()
}