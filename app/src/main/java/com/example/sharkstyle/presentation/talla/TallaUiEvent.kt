package com.example.sharkstyle.presentation.talla

sealed interface TallaUiEvent {
    data class MedidaChange(val medida: String) : TallaUiEvent // Cambiar medida
    object SaveTalla : TallaUiEvent // Guardar talla
    data class DeleteTalla(val tallaId: Int) : TallaUiEvent // Eliminar talla
}
