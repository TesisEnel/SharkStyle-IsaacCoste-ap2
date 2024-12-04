package com.example.sharkstyle.presentation.categoria

import com.example.sharkstyle.data.local.entities.CategoriaEntity
import com.example.sharkstyle.data.remote.dto.CategoriaDto

data class CategoriaUiState(
    val isLoading: Boolean = false,
    val categoriaId: Int? = null,
    val nombre: String = "",
    val imagen: String = "",
    val categorias: List<CategoriaEntity> = emptyList(),
    val errorMessage: String = "",
    val errorNombre: String = "",
    val errorImagen: String = ""
)

fun CategoriaUiState.toDto() = CategoriaDto(
    categoriaId = categoriaId ?: 0,
    nombre = nombre,
    imagen = imagen
)
