package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.DetalleProductoEntity

data class DetalleProductoDto(
    val detalleProductoId: Int,
    val productoId: Int,
    val tallaId: Int,
    val existencia: Int
)

fun DetalleProductoDto.toEntity(): DetalleProductoEntity {
    return DetalleProductoEntity(
        detalleProductoId = detalleProductoId,
        productoId = productoId,
        tallaId = tallaId,
        existencia = existencia
    )
}
