package com.example.sharkstyle.presentation.producto

sealed interface ProductoUiEvent {
    data class TituloChange(val titulo: String) : ProductoUiEvent
    data class PrecioChange(val precio: Double) : ProductoUiEvent
    data class CategoriaIdChange(val categoriaId: Int) : ProductoUiEvent
    data object Save : ProductoUiEvent
    data object Delete : ProductoUiEvent
}
