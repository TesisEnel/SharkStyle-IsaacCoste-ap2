package com.example.sharkstyle.presentation.categoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharkstyle.data.repository.CategoriaRepository
import com.example.sharkstyle.data.repository.ProductoRepository
import com.example.sharkstyle.data.util.Resource
import com.example.sharkstyle.presentation.producto.ProductoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaUiState())
    private val _uiStateProductos = MutableStateFlow(ProductoUiState())
    val uiState = _uiState.asStateFlow()
    val uiStateProductos = _uiStateProductos.asStateFlow()

    init {
        getCategorias()
    }

    private fun getCategorias() {
        viewModelScope.launch {
            categoriaRepository.getCategorias().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                categorias = result.data ?: emptyList()
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

    fun ProductosByCategoria(categoriaId: Int) {
        viewModelScope.launch {
            categoriaRepository.getProductosByCategoria(categoriaId).collectLatest { result ->
                _uiStateProductos.update {
                    when (result) {
                        is Resource.Loading -> it.copy(isLoading = true)
                        is Resource.Success -> it.copy(
                            isLoading = false,
                            productos = result.data ?: emptyList(),
                            errorMessage = ""
                        )
                        is Resource.Error -> it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Error desconocido"
                        )
                    }
                }
            }
        }
    }

    private fun getCategoriaById(id: Int) {
        viewModelScope.launch {
            categoriaRepository.getCategoria(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        result.data?.let { categoria ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    categoriaId = categoria.categoriaId,
                                    nombre = categoria.nombre,
                                    imagen = categoria.imagen
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

    private fun saveCategoria() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (validateFields(currentState)) {
                categoriaRepository.saveCategoria(currentState.toDto()).let { result ->
                    if (result is Resource.Error) {
                        _uiState.update {
                            it.copy(errorMessage = result.message ?: "Error al guardar la categoría")
                        }
                    } else {
                        getCategorias()
                    }
                }
            }
        }
    }

    private fun deleteCategoria(id: Int) {
        viewModelScope.launch {
            categoriaRepository.deleteCategoria(id).let { result ->
                if (result is Resource.Error) {
                    _uiState.update {
                        it.copy(errorMessage = result.message ?: "Error al eliminar la categoría")
                    }
                } else {
                    getCategorias()
                }
            }
        }
    }

    fun onEvent(event: CategoriaUiEvent) {
        when (event) {
            is CategoriaUiEvent.NombreChange -> {
                _uiState.update { it.copy(nombre = event.nombre, errorNombre = "") }
            }
            is CategoriaUiEvent.ImagenChange -> {
                _uiState.update { it.copy(imagen = event.imagen, errorImagen = "") }
            }
            is CategoriaUiEvent.SaveCategoria -> saveCategoria()
            is CategoriaUiEvent.DeleteCategoria -> deleteCategoria(event.categoriaId)
        }
    }

    private fun validateFields(state: CategoriaUiState): Boolean {
        var isValid = true
        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = "El nombre es requerido") }
            isValid = false
        }
        if (state.imagen.isBlank()) {
            _uiState.update { it.copy(errorImagen = "La imagen es requerida") }
            isValid = false
        }
        return isValid
    }
}
