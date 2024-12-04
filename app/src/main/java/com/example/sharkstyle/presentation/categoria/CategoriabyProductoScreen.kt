package com.example.sharkstyle.presentation.categoria

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.pruebasharkstyle.R
import com.example.sharkstyle.data.local.entities.ProductoEntity
import com.example.sharkstyle.presentation.component.NavigationBarWithAnimatedIcons

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProductoCategoriaListScreen(
    viewModel: CategoriaViewModel = hiltViewModel(),
    gotoHome: () -> Unit = {},
    gotoCart: () -> Unit = {},
    gotoUser: () -> Unit = {},
    onProductoClick: (Int) -> Unit = {},
    onCategoriaClick: () -> Unit = {},
    categoriaId: Int
) {
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(categoriaId) {
        viewModel.ProductosByCategoria(categoriaId)
    }
    val uiState = viewModel.uiStateProductos.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    if (isSearchVisible) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.foundation.text.BasicTextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                    if (searchQuery.isEmpty()) isSearchVisible = false
                                },
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Buscar...",
                                            color = Color.Gray,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    } else {
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
                                    .clickable { onCategoriaClick() }
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
                                    .clickable { isSearchVisible = true }
                            )
                        }
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
            val productosFiltrados = uiState.value.productos.filter {
                it.titulo.contains(searchQuery, ignoreCase = true)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(productosFiltrados) { index, producto ->
                    ProductoCategoriaCard(
                        index,
                        producto,
                        onProductoClick
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCategoriaCard(
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