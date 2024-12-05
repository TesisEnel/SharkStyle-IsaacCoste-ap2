package com.example.sharkstyle.presentation.talla

import com.example.sharkstyle.data.local.entities.TallaEntity
import com.example.sharkstyle.data.remote.dto.TallaDto

data class TallaUiState(
    val isLoading: Boolean = false, // Estado de carga
    val tallaId: Int = 0, // ID de la talla
    val medida: String = "", // Medida de la talla
    val tallas: List<TallaEntity> = emptyList(), // Lista de tallas cargadas
    val errorMessage: String = "" // Mensaje de error
)

// Función para convertir el estado a un DTO
fun TallaUiState.toDto() = TallaDto(
    tallaId = tallaId,
    medida = medida
)
