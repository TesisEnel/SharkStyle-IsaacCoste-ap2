package com.example.sharkstyle.presentation.home

sealed interface HomeUiEvent {
    data class OnProductoClick(val productoId: Int) : HomeUiEvent
}