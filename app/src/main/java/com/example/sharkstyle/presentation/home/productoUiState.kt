package com.example.sharkstyle.presentation.home

import com.example.sharkstyle.data.local.entities.ProductoEntity

data class productoUiState(
    val isLoading: Boolean = false,
//    val productoId: Int,
//    val titulo: String,
//    val categoriaId: Int,
//    val precio: Double,
//    val descripcion: String,
//    val existencia: Int,
//    val imagen: String,
//    val impuesto: Double,
    val productos: List<ProductoEntity> = emptyList(),
//    val detalleProducto: List<DetalleProductoEntity> = listOf(),
    val errorMessage: String = ""
)
