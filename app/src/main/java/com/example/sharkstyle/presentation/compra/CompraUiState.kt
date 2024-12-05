package com.example.sharkstyle.presentation.compra

import com.example.sharkstyle.data.local.entities.CompraEntity
import com.example.sharkstyle.data.local.entities.DetalleCompraEntity
import com.example.sharkstyle.data.remote.dto.CompraDto

data class CompraUiState(
    val isLoading: Boolean = false, // Indicador de carga
    val compraId: Int? = null, // ID de la compra
    val usuarioId: Int? = null, // ID del usuario asociado a la compra
    val total: Double = 0.0, // Total de la compra
    val detallesCompra: List<DetalleCompraEntity> = emptyList(), // Detalles de la compra
    val compras: List<CompraEntity> = emptyList(), // Lista de compras
    val errorMessage: String = "", // Mensaje de error general
    val errorUsuarioId: String = "", // Error en el campo usuarioId
    val errorDetalles: String = "", // Error en los detalles de la compra
    val errorTotal: String = "" // Error en el total
)

// Conversión del estado a un DTO
fun CompraUiState.toDto() = CompraDto(
    compraId = compraId ?: 0,
    usuarioId = usuarioId ?: 0,
    total = total,
    detallesCompra = detallesCompra
)