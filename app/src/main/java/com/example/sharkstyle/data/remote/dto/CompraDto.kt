package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.CompraEntity
import com.example.sharkstyle.data.local.entities.DetalleCompraEntity

data class CompraDto(
    val compraId: Int,
    val usuarioId: Int,
    val total: Double,
    val detallesCompra: List<DetalleCompraEntity> = mutableListOf()
)

fun CompraDto.toEntity(): CompraEntity {
    return CompraEntity(
        compraId = compraId,
        usuarioId = usuarioId,
        total = total,
        detallesCompra = detallesCompra
    )
}
