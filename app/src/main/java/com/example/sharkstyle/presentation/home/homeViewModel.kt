package com.example.sharkstyle.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharkstyle.data.repository.CategoriaRepository
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
class homeViewModel @Inject constructor(
    private val ProductoRepository: ProductoRepository,
    private val CategoriaRepository: CategoriaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(productoUiState())
    private val _uiStateCategoria = MutableStateFlow(categoriaUiState())
    val uiState = _uiState.asStateFlow()
    val uiStateCategoria = _uiStateCategoria.asStateFlow()

    init {
        getProductos()
        getCategorias()
    }

    private fun getProductos() {
        viewModelScope.launch {
            ProductoRepository.getProductos().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
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

//    private fun getProducto(id: Int) {
//        viewModelScope.launch {
//            ProductoRepository.getProducto(id).collectLatest { result ->
//                when (result) {
//                    is Resource.Loading -> {
//                        _uiState.update {
//                            it.copy(isLoading = true)
//                        }
//                    }
//
//                    is Resource.Success -> {
//                        result.data?.let { producto ->
//                            _uiState.update {
//                                it.copy(
//                                    isLoading = false,
//                                    productoId = producto.productoId,
//                                    titulo = producto.titulo,
//                                    categoriaId = producto.categoriaId,
//                                    precio = producto.precio,
//                                    descripcion = producto.descripcion,
//                                    existencia = producto.existencia,
//                                    imagen = producto.imagen,
//                                    impuesto = producto.impuesto
//                                )
//                            }
//                        }
//                    }
//                    is Resource.Error -> {
//                        _uiState.update {
//                            it.copy(
//                                isLoading = false,
//                                errorMessage = result.message ?: "Error desconocido"
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun getCategorias() {
        viewModelScope.launch {
            CategoriaRepository.getCategorias().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiStateCategoria.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiStateCategoria.update {
                            it.copy(
                                isLoading = false,
                                categorias = result.data ?: emptyList()
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiStateCategoria.update {
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
}