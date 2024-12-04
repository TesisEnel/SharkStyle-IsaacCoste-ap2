package com.example.sharkstyle.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object CarritoList : Screen()

    @Serializable
    data object ProductoList : Screen()

    @Serializable
    data object CategoriaList : Screen()

    @Serializable
    data object CompraList : Screen()

    @Serializable
    data object UsuarioList : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Signup : Screen()

    @Serializable
    data object NavBar : Screen()

    @Serializable
    data class Producto(val productoId: Int) : Screen()

    @Serializable
    data class Categoria(val categoriaId: Int) : Screen()

    @Serializable
    data object Cart : Screen()

    @Serializable
    data class User(val userId: Int) : Screen()

    @Serializable
    data object Talla : Screen()

}