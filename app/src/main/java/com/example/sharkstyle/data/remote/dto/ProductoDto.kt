package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.ProductoEntity


data class ProductoDto(
    val productoId: Int,
    val titulo: String,
    val categoriaId: Int,
    val precio: Double,
    val descripcion: String,
    val existencia: Int,
    val imagen: String,
    val impuesto: Double,
    val detalleProducto: MutableList<DetalleProductoDto> = mutableListOf()
)

fun ProductoDto.toEntity(): ProductoEntity {
    return ProductoEntity(
        productoId = productoId,
        titulo = titulo,
        categoriaId = categoriaId,
        precio = precio,
        descripcion = descripcion,
        existencia = existencia,
        imagen = imagen,
        impuesto = impuesto,
        detalleProducto = detalleProducto.map { it.toEntity() }.toMutableList()
    )
}