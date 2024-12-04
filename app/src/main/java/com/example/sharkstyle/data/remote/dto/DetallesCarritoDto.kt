package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity

data class DetallesCarritoDto(
    val detalleCarritoId: Int,
    val carritoId: Int,
    val productoId: Int,
    val cantidad: Int,
    val precio: Double,
)

fun DetallesCarritoDto.toEntity(): DetalleCarritoEntity {
    return DetalleCarritoEntity(
        detalleCarritoId = detalleCarritoId,
        carritoId = carritoId,
        productoId = productoId,
        cantidad = cantidad,
        precio = precio
    )
}
