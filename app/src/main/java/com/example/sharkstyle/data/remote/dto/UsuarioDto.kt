package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.UsuarioEntity

data class UsuarioDto(
    val usuarioId: Int,
    val nombre: String,
    val email: String,
    val password: String,
)

fun UsuarioDto.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        usuarioId = usuarioId,
        nombre = nombre,
        email = email,
        password = password
    )
}