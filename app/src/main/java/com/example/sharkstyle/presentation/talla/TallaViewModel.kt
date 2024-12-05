package com.example.sharkstyle.presentation.talla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharkstyle.data.repository.TallaRepository
import com.example.sharkstyle.data.util.Resource
import com.example.sharkstyle.presentation.talla.TallaUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TallaViewModel @Inject constructor(
    private val tallaRepository: TallaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TallaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTallas()
    }

    // Obtener todas las tallas
    private fun getTallas() {
        viewModelScope.launch {
            tallaRepository.getTallas().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                tallas = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    // Obtener una talla específica
    private fun getTallaById(id: Int) {
        viewModelScope.launch {
            tallaRepository.getTalla(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        result.data?.let { talla ->
                            _uiState.update {
                                it.copy(
                                    tallaId = talla.tallaId,
                                    medida = talla.medida,
                                    isLoading = false
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    // Manejo de eventos
    fun onEvent(event: TallaUiEvent) {
        when (event) {
            is TallaUiEvent.MedidaChange -> {
                _uiState.update { it.copy(medida = event.medida, errorMessage = "") }
            }
            is TallaUiEvent.SaveTalla -> saveTalla()
            is TallaUiEvent.DeleteTalla -> deleteTalla(event.tallaId)
        }
    }

    // Guardar una talla
    private fun saveTalla() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (validateFields(currentState)) {
                tallaRepository.saveTalla(currentState.toDto()).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is Resource.Success -> {
                            _uiState.update { it.copy(isLoading = false) }
                            getTallas() // Refrescar lista después de guardar
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message ?: "Error al guardar la talla"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Eliminar una talla
    private fun deleteTalla(id: Int) {
        viewModelScope.launch {
            val result = tallaRepository.deleteTalla(id)
            if (result is Resource.Error) {
                _uiState.update {
                    it.copy(errorMessage = result.message ?: "Error al eliminar la talla")
                }
            } else {
                getTallas() // Refrescar lista después de eliminar
            }
        }
    }

    // Validación de campos
    private fun validateFields(state: TallaUiState): Boolean {
        var isValid = true
        if (state.medida.isBlank()) {
            _uiState.update { it.copy(errorMessage = "La medida no puede estar vacía") }
            isValid = false
        }
        return isValid
    }
}
