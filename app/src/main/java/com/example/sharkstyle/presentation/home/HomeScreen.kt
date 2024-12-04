package com.example.sharkstyle.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.pruebasharkstyle.R
import com.example.sharkstyle.data.local.entities.CategoriaEntity
import com.example.sharkstyle.data.local.entities.ProductoEntity
import com.example.sharkstyle.presentation.component.NavigationBarWithAnimatedIcons
import com.example.sharkstyle.presentation.home.categoriaUiState
import com.example.sharkstyle.presentation.home.homeViewModel
import com.example.sharkstyle.presentation.usuario.UsuarioViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun HomeScreen(
    viewModel: homeViewModel = hiltViewModel(),
    userViewModel: UsuarioViewModel = hiltViewModel(),
    gotoHome: () -> Unit = {},
    gotoCart: () -> Unit = {},
    gotoUser: (Int) -> Unit = {},
    onProductoClick: (Int) -> Unit,
    onCategoriaClick: (Int) -> Unit,
    scope: CoroutineScope,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiStateCategoria by viewModel.uiStateCategoria.collectAsStateWithLifecycle()
    val userEmail by userViewModel.userEmail.collectAsState()
    val userName = userViewModel.getUserNameByEmail(userEmail ?: "")
    val userId = userViewModel.getUsuarioById(userEmail ?: "")

    LaunchedEffect(Unit) {
        userViewModel.getUsuarios()
    }
    HomeBodyScreen(
        uiState = uiState,
        uiStateCategoria = uiStateCategoria,
        scope = scope,
        isLoading = uiState.isLoading,
        gotoHome = gotoHome,
        gotoCart = gotoCart,
        gotoUser = { gotoUser(userId) },
        onProductoClick = onProductoClick,
        onCategoriaClick = onCategoriaClick,
        userName = userName,
        userId = userId
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBodyScreen(
    uiState: productoUiState,
    uiStateCategoria: categoriaUiState,
    scope: CoroutineScope,
    isLoading: Boolean,
    gotoHome: () -> Unit,
    gotoCart: () -> Unit,
    gotoUser: (Int) -> Unit,
    onProductoClick: (Int) -> Unit,
    onCategoriaClick: (Int) -> Unit,
    userName: String,
    userId: Int
) {

    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchVisible) {
                        Box(
                            modifier = Modifier
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            androidx.compose.foundation.text.BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
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
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            AsyncImage(
                                model = "https://sharkstyleimagenes.blob.core.windows.net/sharkstyle/logoapppng.png",
                                contentDescription = "Logo SharkStyle",
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Bienvenido $userName",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black
                            )
                        }
                    }
                },
                actions = {
                    if (!isSearchVisible) {
                        Image(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "Search",
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.White, shape = RoundedCornerShape(50.dp))
                                .clickable { isSearchVisible = !isSearchVisible }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.white))
            )
        },
        bottomBar = {
            NavigationBarWithAnimatedIcons(
                gotoHome = gotoHome,
                gotoCart = gotoCart,
                gotoUser = { gotoUser(userId) }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {

            val scrollState = rememberScrollState()
            val productosFiltrados = uiState.productos.filter {
                it.titulo.contains(searchQuery, ignoreCase = true)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (searchQuery.isNotEmpty() && productosFiltrados.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Resultados de búsqueda:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(productosFiltrados) { producto ->
                                    ProductoCard(
                                        productos = producto,
                                        onProductoClick = onProductoClick
                                    )
                                }
                            }
                        }
                    }
                }

                val productosAleatorios = uiState.productos.shuffled().take(5)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFF000000), shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    if (productosAleatorios.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(productosAleatorios) { producto ->
                                ProductoCard(
                                    productos = producto,
                                    onProductoClick = onProductoClick
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No hay productos disponibles.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }



                Text(
                    text = "Categorias",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (uiStateCategoria.isLoading) {
                    Text(
                        text = "Cargando categorías...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                } else if (uiStateCategoria.categorias.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiStateCategoria.categorias) { categoria ->
                            CategoriaCard(
                                categoria = categoria,
                                onCategoriaClick = onCategoriaClick
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No hay categorías disponibles.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Text(
                    text = "Recomendados para ti",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (uiState.isLoading) {
                    Text(
                        text = "Cargando productos...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                } else if (uiState.productos.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.productos) { producto ->
                            ProductoCard(productos = producto, onProductoClick = onProductoClick)
                        }
                    }
                } else {
                    Text(
                        text = "No hay productos disponibles.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriaCard(
    categoria: CategoriaEntity,
    onCategoriaClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { onCategoriaClick(categoria.categoriaId) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(categoria.imagen),
                contentDescription = categoria.nombre,
                modifier = Modifier.size(90.dp)
            )
            Text(
                text = categoria.nombre,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ProductoCard(
    productos: ProductoEntity,
    onProductoClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .width(168.dp)
            .clickable { onProductoClick(productos.productoId) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.background(Color.White)) {
            Image(
                painter = rememberAsyncImagePainter(productos.imagen),
                contentDescription = productos.titulo,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = productos.titulo,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = "RD$${productos.precio}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp, start = 5.dp)
            )
        }
    }
}
