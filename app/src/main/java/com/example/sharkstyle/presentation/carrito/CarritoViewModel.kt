package com.example.sharkstyle.presentation.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharkstyle.data.local.entities.CarritoEntity
import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity
import com.example.sharkstyle.data.remote.dto.CarritoDto
import com.example.sharkstyle.data.repository.AuthRepository
import com.example.sharkstyle.data.repository.CarritoRepository
import com.example.sharkstyle.data.repository.ProductoRepository
import com.example.sharkstyle.data.repository.UsuarioRepository
import com.example.sharkstyle.data.util.Resource
import com.example.sharkstyle.presentation.usuario.UsuarioUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository,
    private val productoRepository: ProductoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CarritoUiState(
        usuarioId = 1
    )
    )
    val uiState = _uiState.asStateFlow()
    private val _uiStateUsuario = MutableStateFlow(UsuarioUiState())
    val uiStateUsuario = _uiStateUsuario.asStateFlow()
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail

    init {
        obtenerUsuarioActual()
    }

    fun fetchUserEmail() {
        _userEmail.value = authRepository.getUser()
    }
    fun getUserNameByEmail(email: String): String {
        val user = _uiStateUsuario.value.usuarios.find { it.email == email }
        return user?.nombre ?: "Usuario desconocido"
    }
    fun getUsuarioById(email: String): Int {
        val usuario = _uiStateUsuario.value.usuarios.find { it.email == email }
        return usuario?.usuarioId ?: 0
    }
    
    private fun obtenerUsuarioActual() {
        viewModelScope.launch {
            val currentUserEmail = authRepository.getUser()
            val usuario = usuarioRepository.getUsuarioByCorreo(currentUserEmail ?: "")
            if (usuario != null) {
                _uiState.update { it.copy(usuarioId = 1) }
                cargarDatosDelCarrito(1)
            } else {
            }
        }
    }

    private fun cargarDatosDelCarrito(usuarioId: Int) {
        getCarritos(1)
        getCarritoDetalles(1)
    }

    private fun getCarritos(usuarioId: Int) {
        viewModelScope.launch {
            carritoRepository.getCarritosPorUsuario(usuarioId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                carritos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }


    private fun getCarritoDetalles(usuarioId: Int) {
        viewModelScope.launch {
            val carrito = carritoRepository.getLastCarritoByPersona(usuarioId)
            if (carrito != null) {
                carritoRepository.getCarritoDetallesPorCarritoId(carrito.carritoId!!).collect { detalles ->
                    _uiState.update {
                        it.copy(detallesCarrito = detalles.toMutableList())
                    }
                }
            } else {
                _uiState.update { it.copy(detallesCarrito = emptyList()) }
            }
        }
    }

    private suspend fun agregarProducto(carridetalle: DetalleCarritoEntity, cantidad: Int) {
        var carritoAnterior = carritoRepository.getCarritoNoPagadoPorUsuario(1)
        if (carritoAnterior == null) {
            _uiState.value.usuarioId?.let {
                CarritoEntity(
                    usuarioId = it,
                    pagado = false,
                    detallesCarrito = mutableListOf(),
                )
            }?.let {
                carritoRepository.saveCarrito(
                    it
                )
            }
            carritoAnterior = carritoRepository.getCarritoNoPagadoPorUsuario(_uiState.value.usuarioId!!)
        }
        val existeCarrito = carritoRepository.CarritoExiste(
            carridetalle.productoId ?: 0,
            carritoAnterior?.carritoId ?: 0
        )
        val producto = productoRepository.getProducto(carridetalle.productoId ?: 0)
        if (existeCarrito.equals(false)) {
            carritoRepository.addCarritoDetalle(
                DetalleCarritoEntity(
                    carritoId = carritoAnterior?.carritoId ?: 0,
                    productoId = carridetalle.productoId ?: 0,
                    cantidad = cantidad,
                    precio = carridetalle.precio
                )
            )
        } else {
            val carriDetalleRepetido = carritoRepository.getCarritoDetalleByProductoId(
                carridetalle.productoId ?: 0,
                carritoAnterior?.carritoId ?: 0
            )
            val nuevaCantidad = (carriDetalleRepetido?.cantidad ?: 0) + (carridetalle.cantidad ?: 0)

            carritoRepository.addCarritoDetalle(
                DetalleCarritoEntity(
                    carritoId = carritoAnterior?.carritoId ?: 0,
                    productoId = carridetalle.productoId ?: 0,
                    cantidad = nuevaCantidad,
                    precio = carridetalle.precio
                )
            )
        }
    }
    // Obtener un carrito por ID
//    private fun getCarritoById(id: Int) {
//        viewModelScope.launch {
//            carritoRepository.getCarrito(id).collectLatest { result ->
//                when (result) {
//                    is Resource.Loading -> {
//                        _uiState.update { it.copy(isLoading = true) }
//                    }
//                    is Resource.Success -> {
//                        result.data?.let { carrito ->
//                            _uiState.update {
//                                it.copy(
//                                    isLoading = false,
//                                    carritoId = carrito.carritoId,
//                                    usuarioId = carrito.usuarioId,
//                                    detallesCarrito = carrito.detallesCarrito
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


    private fun saveCarrito() {
        viewModelScope.launch {
            try {
                val carritoDto = CarritoDto(
                    carritoId = _uiState.value.carritoId ?: 0,
                    usuarioId = _uiState.value.usuarioId!!,
                    pagado = false,
                    detallesCarrito = _uiState.value.detallesCarrito
                )
                carritoRepository.addCarritoApi(carritoDto)
                _uiState.update { it.copy(success = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Error al guardar el carrito") }
            }
        }
    }

    private fun deleteCarrito(id: Int) {
//        viewModelScope.launch {
//            carritoRepository.deleteCarrito(id).let { result ->
//                if (result is Resource.Error) {
//                    _uiState.update {
//                        it.copy(errorMessage = result.message ?: "Error al eliminar el carrito")
//                    }
//                } else {
//                    getCarritos() // Refrescar la lista tras eliminar
//                }
//            }
//        }
    }

    suspend fun onEvent(event: CarritoUiEvent) {
        when (event) {
            is CarritoUiEvent.UsuarioIdChange -> {
                _uiState.update { it.copy(usuarioId = event.usuarioId, errorUsuarioId = "") }
            }
            is CarritoUiEvent.DetallesCarritoChange -> {
                _uiState.update { it.copy(detallesCarrito = event.detalles, errorDetalles = "") }
            }
            is CarritoUiEvent.SaveCarrito -> saveCarrito()
            is CarritoUiEvent.DeleteCarrito -> deleteCarrito(event.carritoId)
            is CarritoUiEvent.AgregarProducto -> agregarProducto(event.producto, event.cantidad)
        }
    }

    private fun validateFields(state: CarritoUiState): Boolean {
        var isValid = true
        if (state.usuarioId == null || state.usuarioId <= 0) {
            _uiState.update { it.copy(errorUsuarioId = "El ID de usuario es requerido y debe ser válido") }
            isValid = false
        }
        if (state.detallesCarrito.isEmpty()) {
            _uiState.update { it.copy(errorDetalles = "Debe haber al menos un detalle en el carrito") }
            isValid = false
        }
        return isValid
    }
}
