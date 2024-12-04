package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.CategoriaEntity

data class CategoriaDto(
    val categoriaId: Int,
    val nombre: String,
    val imagen: String,
)

fun CategoriaDto.toEntity(): CategoriaEntity {
    return CategoriaEntity(
        categoriaId = categoriaId,
        nombre = nombre,
        imagen = imagen
    )
}
