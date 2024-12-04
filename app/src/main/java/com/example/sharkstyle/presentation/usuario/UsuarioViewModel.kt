package com.example.sharkstyle.presentation.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharkstyle.data.remote.dto.UsuarioDto
import com.example.sharkstyle.data.repository.AuthRepository
import com.example.sharkstyle.data.repository.UsuarioRepository
import com.example.sharkstyle.data.util.Resource
import com.example.sharkstyle.presentation.login.AuthState
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState = _uiState.asStateFlow()
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail

    init {
        getUsuarios()
        fetchUserEmail()
    }

    fun fetchUserEmail() {
        _userEmail.value = authRepository.getUser()
    }
    fun getUserNameByEmail(email: String): String {
        val user = _uiState.value.usuarios.find { it.email == email }
        return user?.nombre ?: "Usuario desconocido"
    }
    fun getUsuarioById(email: String): Int {
        val usuario = _uiState.value.usuarios.find { it.email == email }
        return usuario?.usuarioId ?: 0
    }

    fun getUsuarios() {
        viewModelScope.launch {
            usuarioRepository.getUsuarios().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                usuarios = result.data ?: emptyList()
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

    fun getUsuarioById(id: Int) {
        viewModelScope.launch {
            usuarioRepository.getUsuario(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        result.data?.let { usuario ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    usuarioId = usuario.usuarioId,
                                    nombre = usuario.nombre,
                                    email = usuario.email,
                                    password = usuario.password
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

    fun saveUsuario() {
        if (!validateFields(_uiState.value)) return

        viewModelScope.launch {
            val existingUsuario = usuarioRepository.getUsuarioByCorreo(_uiState.value.email)
            if (existingUsuario != null) {
                _uiState.update {
                    it.copy(errorEmail = "Intente con otro email")
                }
                return@launch
            }

            usuarioRepository.save(_uiState.value.toDto()).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "",
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido",
                            )
                        }
                    }
                }
            }
        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
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

    fun signup(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.signUp(email, password).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Error desconocido"
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }
                }
            }
            usuarioRepository.save(_uiState.value.toDto())
        }
    }

    fun signOut() {
        authRepository.logout()
    }

    fun onEvent(event: UsuarioUiEvent) {
        when (event) {
            is UsuarioUiEvent.NombreChange -> {
                _uiState.update { it.copy(nombre = event.nombre, errorNombre = "") }
            }
            is UsuarioUiEvent.EmailChange -> {
                _uiState.update { it.copy(email = event.email, errorEmail = "") }
            }
            is UsuarioUiEvent.PasswordChange -> {
                _uiState.update { it.copy(password = event.password, errorPassword = "") }
            }
            is UsuarioUiEvent.SaveUsuario -> saveUsuario()
        }
    }

    private fun validateFields(state: UsuarioUiState): Boolean {
        var isValid = true
        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = "El nombre es requerido") }
            isValid = false
        }
        if (state.email.isBlank() || !isValidEmail(state.email)) {
            _uiState.update {
                it.copy(errorEmail = "El formato del email no es válido")
            }
            isValid = false
        }
        if (state.password.isBlank() || state.password.length < 6) {
            _uiState.update { it.copy(errorPassword = "La contraseña debe tener al menos 6 caracteres") }
            isValid = false
        }
        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[\\p{L}0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.matches(emailPattern.toRegex())
    }
    fun onNameChange(Nombre: String) {
        _uiState.update { it.copy(nombre = Nombre) }
    }
    fun onEmailChange(Email: String) {
        _uiState.update { it.copy(email = Email) }
    }
    fun onPasswordChange(Password: String) {
        _uiState.update { it.copy(password = Password) }
    }
}

