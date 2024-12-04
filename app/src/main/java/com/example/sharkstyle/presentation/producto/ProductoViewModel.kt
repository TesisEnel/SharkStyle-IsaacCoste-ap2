package com.example.sharkstyle.presentation.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharkstyle.data.repository.ProductoRepository
import com.example.sharkstyle.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getProductos()
    }

    private fun getProductos() {
        viewModelScope.launch {
            productoRepository.getProductos().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                productos = result.data ?: emptyList()
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

 fun getProducto(id: Int) {
        viewModelScope.launch {
            productoRepository.getProducto(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        result.data?.let { producto ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    productoId = producto.productoId,
                                    titulo = producto.titulo,
                                    categoriaId = producto.categoriaId,
                                    precio = producto.precio,
                                    descripcion = producto.descripcion,
                                    imagen = producto.imagen,
                                    impuesto = producto.impuesto,
                                    existencia = producto.existencia
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

    fun onEvent(event: ProductoUiEvent) {
        when (event) {
            is ProductoUiEvent.TituloChange -> {
                _uiState.update {
                    it.copy(
                        titulo = event.titulo,
                        errorTitulo = ""
                    )
                }
            }
            is ProductoUiEvent.PrecioChange -> {
                _uiState.update {
                    it.copy(
                        precio = event.precio,
                        errorPrecio = ""
                    )
                }
            }
            is ProductoUiEvent.CategoriaIdChange -> {
                _uiState.update {
                    it.copy(
                        categoriaId = event.categoriaId,
                        errorCategoriaId = ""
                    )
                }
            }
            is ProductoUiEvent.Save -> {
                saveProducto()
            }
            is ProductoUiEvent.Delete -> {
                deleteProducto()
            }
        }
    }

    private fun saveProducto() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (validateFields(currentState)) {
                val productoDto = currentState.toEntity()
                val result = productoRepository.saveProducto(productoDto)
                if (result is Resource.Error) {
                    _uiState.update {
                        it.copy(errorMessage = result.message ?: "Error al guardar el producto")
                    }
                } else {
                    getProductos()
                }
            }
        }
    }

    private fun deleteProducto() {
        viewModelScope.launch {
            val productoId = _uiState.value.productoId
            if (productoId != null) {
                val result = productoRepository.deleteProducto(productoId)
                if (result is Resource.Error) {
                    _uiState.update {
                        it.copy(errorMessage = result.message ?: "Error al eliminar el producto")
                    }
                } else {
                    getProductos()
                }
            }
        }
    }

    private fun validateFields(state: ProductoUiState): Boolean {
        var isValid = true
        if (state.titulo.isBlank()) {
            _uiState.update { it.copy(errorTitulo = "El título no puede estar vacío") }
            isValid = false
        }
        if (state.precio <= 0) {
            _uiState.update { it.copy(errorPrecio = "El precio debe ser mayor a 0") }
            isValid = false
        }
        return isValid
    }
}
