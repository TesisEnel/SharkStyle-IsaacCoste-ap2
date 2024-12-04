package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.CarritoEntity
import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity

data class CarritoDto(
    val carritoId: Int,
    val usuarioId: Int,
    val pagado: Boolean,
    val detallesCarrito: List<DetalleCarritoEntity> = mutableListOf()
)

fun CarritoDto.toEntity(): CarritoEntity {
    return CarritoEntity(
        carritoId = carritoId,
        usuarioId = usuarioId,
        pagado = false,
        detallesCarrito = detallesCarrito
    )
}