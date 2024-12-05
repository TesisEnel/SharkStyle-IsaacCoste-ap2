package com.example.sharkstyle.presentation.compra


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharkstyle.data.repository.CompraRepository
import com.example.sharkstyle.data.util.Resource
import com.example.sharkstyle.presentation.compra.CompraUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompraViewModel @Inject constructor(
    private val compraRepository: CompraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompraUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCompras()
    }

    // Obtener todas las compras
    private fun getCompras() {
        viewModelScope.launch {
            compraRepository.getCompras().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                compras = result.data ?: emptyList()
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido"
                            )
                        }
                    }
                }
            }
        }
    }

    // Obtener una compra por ID
    private fun getCompraById(id: Int) {
        viewModelScope.launch {
            compraRepository.getCompra(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        result.data?.let { compra ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    compraId = compra.compraId,
                                    usuarioId = compra.usuarioId,
                                    detallesCompra = compra.detallesCompra
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido"
                            )
                        }
                    }
                }
            }
        }
    }

    // Guardar una compra
    private fun saveCompra() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (validateFields(currentState)) {
                val compraDto = currentState.toDto()
                val result = compraRepository.saveCompra(compraDto)
                when (result) {
                    is Resource.Success -> {
                        getCompras() // Refrescar la lista tras guardar
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = result.message ?: "Error al guardar la compra")
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun deleteCompra(id: Int) {
        viewModelScope.launch {
            val result = compraRepository.deleteCompra(id)
            if (result is Resource.Error) {
                _uiState.update {
                    it.copy(errorMessage = result.message ?: "Error al eliminar la compra")
                }
            } else {
                getCompras() // Refrescar la lista tras eliminar
            }
        }
    }

    // Manejar eventos de la UI
    fun onEvent(event: CompraUiEvent) {
        when (event) {
            is CompraUiEvent.UsuarioIdChange -> {
                _uiState.update { it.copy(usuarioId = event.usuarioId, errorUsuarioId = "") }
            }
            is CompraUiEvent.DetallesCompraChange -> {
                _uiState.update { it.copy(detallesCompra = event.detalles, errorDetalles = "") }
            }
            is CompraUiEvent.SaveCompra -> saveCompra()
            is CompraUiEvent.DeleteCompra -> deleteCompra(event.compraId)
            is CompraUiEvent.TotalChange -> {
                _uiState.update { it.copy(total = event.total, errorTotal = "") }
            }
        }
    }

    private fun validateFields(state: CompraUiState): Boolean {
        var isValid = true
        if (state.usuarioId == null || state.usuarioId <= 0) {
            _uiState.update { it.copy(errorUsuarioId = "El ID de usuario es requerido y debe ser válido") }
            isValid = false
        }
        if (state.detallesCompra.isEmpty()) {
            _uiState.update { it.copy(errorDetalles = "Debe haber al menos un detalle en la compra") }
            isValid = false
        }
        return isValid
    }
}