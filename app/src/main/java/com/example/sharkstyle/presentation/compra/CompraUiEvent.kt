package com.example.sharkstyle.presentation.compra

import com.example.sharkstyle.data.local.entities.DetalleCompraEntity

sealed interface CompraUiEvent {
    data class UsuarioIdChange(val usuarioId: Int) : CompraUiEvent
    data class TotalChange(val total: Double) : CompraUiEvent
    data class DetallesCompraChange(val detalles: List<DetalleCompraEntity>) : CompraUiEvent
    object SaveCompra : CompraUiEvent
    data class DeleteCompra(val compraId: Int) : CompraUiEvent
}