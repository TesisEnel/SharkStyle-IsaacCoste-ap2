package com.example.sharkstyle.presentation.carrito

import com.example.sharkstyle.data.local.entities.CarritoEntity
import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity
import com.example.sharkstyle.data.remote.dto.CarritoDto

data class CarritoUiState(
    val isLoading: Boolean = false,
    val carritoId: Int? = null,
    val usuarioId: Int?,
    val detallesCarrito: List<DetalleCarritoEntity> = emptyList(),
    val carritos: List<CarritoEntity> = emptyList(),
    val errorMessage: String = "",
    val errorUsuarioId: String = "",
    val errorDetalles: String = "",
    val success: Boolean = false
)

fun CarritoUiState.toDto() = CarritoDto(
    carritoId = carritoId ?: 0,
    usuarioId = usuarioId ?: 0,
    pagado = false,
    detallesCarrito = detallesCarrito
)
