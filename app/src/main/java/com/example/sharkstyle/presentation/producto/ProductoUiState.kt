package com.example.sharkstyle.presentation.producto

import com.example.sharkstyle.data.local.entities.DetalleProductoEntity
import com.example.sharkstyle.data.local.entities.ProductoEntity
import com.example.sharkstyle.data.remote.dto.ProductoDto

data class ProductoUiState(
    val isLoading: Boolean = false,
    val productoId: Int? = null,
    val titulo: String = "",
    val categoriaId: Int? = null,
    val precio: Double = 0.0,
    val descripcion: String = "",
    val imagen: String = "",
    val impuesto: Double = 0.0,
    val existencia: Int = 0,
    val detalleProducto: List<DetalleProductoEntity> = emptyList(),
    val productos: List<ProductoEntity> = emptyList(),
    val errorTitulo: String = "",
    val errorPrecio: String = "",
    val errorCategoriaId: String = "",
    val errorMessage: String = ""
)

fun ProductoUiState.toEntity() = ProductoDto(
    productoId = productoId ?: 0,
    titulo = titulo,
    categoriaId = categoriaId ?: 0,
    precio = precio,
    descripcion = descripcion,
    imagen = imagen,
    impuesto = impuesto,
    existencia = existencia
)
