package com.example.sharkstyle.presentation.categoria

sealed interface CategoriaUiEvent {
    data class NombreChange(val nombre: String) : CategoriaUiEvent
    data class ImagenChange(val imagen: String) : CategoriaUiEvent
    object SaveCategoria : CategoriaUiEvent
    data class DeleteCategoria(val categoriaId: Int) : CategoriaUiEvent
}
