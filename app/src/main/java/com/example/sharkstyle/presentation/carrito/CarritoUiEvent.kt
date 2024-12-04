package com.example.sharkstyle.presentation.carrito

import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity

sealed interface CarritoUiEvent {
    data class UsuarioIdChange(val usuarioId: Int) : CarritoUiEvent
    data class DetallesCarritoChange(val detalles: List<DetalleCarritoEntity>) : CarritoUiEvent
    object SaveCarrito : CarritoUiEvent
    data class DeleteCarrito(val carritoId: Int) : CarritoUiEvent
    data class AgregarProducto(val producto: DetalleCarritoEntity, val cantidad: Int) :
        CarritoUiEvent
}
