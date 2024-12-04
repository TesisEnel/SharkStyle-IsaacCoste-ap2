package com.example.sharkstyle.presentation.usuario

import com.example.sharkstyle.data.local.entities.UsuarioEntity
import com.example.sharkstyle.data.remote.dto.UsuarioDto

data class UsuarioUiState(
    val isLoading: Boolean = false,
    val usuarioId: Int = 0,
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val usuarios: List<UsuarioEntity> = emptyList(),
    val errorMessage: String = "",
    val errorNombre: String = "",
    val errorEmail: String = "",
    val errorPassword: String = ""
)

fun UsuarioUiState.toDto() = UsuarioDto(
    usuarioId = usuarioId,
    nombre = nombre,
    email = email,
    password = password
)