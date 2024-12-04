package com.example.sharkstyle.presentation.home

import com.example.sharkstyle.data.local.entities.CategoriaEntity

data class categoriaUiState(
    val isLoading: Boolean = false,
//    val categoriaId: Int = 0,
//    val nombre: String = "",
//    val imagen: String = "",
    val categorias: List<CategoriaEntity> = emptyList(),
    val errorMessage: String = "",
//    val errorNombre: String = "",
//    val errorImagen: String = ""
)
