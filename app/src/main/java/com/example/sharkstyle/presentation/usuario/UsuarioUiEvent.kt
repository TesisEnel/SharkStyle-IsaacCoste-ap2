package com.example.sharkstyle.presentation.usuario

sealed class UsuarioUiEvent {
    data class NombreChange(val nombre: String) : UsuarioUiEvent()
    data class EmailChange(val email: String) : UsuarioUiEvent()
    data class PasswordChange(val password: String) : UsuarioUiEvent()
    data class SaveUsuario(val usuarioId: Int) : UsuarioUiEvent()
}
