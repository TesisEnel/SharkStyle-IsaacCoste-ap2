package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.DetalleCompraEntity

data class DetallesCompraDto(
    val detalleCompraId: Int,
    val compraId: Int,
    val productoId: Int,
    val cantidad: Int,
)

fun DetallesCompraDto.toEntity(): DetalleCompraEntity {
    return DetalleCompraEntity(
        detalleCompraId = detalleCompraId,
        compraId = compraId,
        productoId = productoId,
        cantidad = cantidad
    )
}
