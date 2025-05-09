package com.pri.artsysearchapp.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pri.artsysearchapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onFullNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value, fullNameError = null)
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value, emailError = null)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value, passwordError = null)
    }

    fun validateFullName() {
        if (_uiState.value.fullName.isBlank()) {
            _uiState.value = _uiState.value.copy(fullNameError = "Full name cannot be empty")
        }
    }

    fun validateEmail(validatePattern: Boolean = false) {
        val email = _uiState.value.email
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Email cannot be empty")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(emailError = "Invalid email format")
        }
    }

    fun validatePassword() {
        if (_uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "Password cannot be empty")
        }
    }

    fun onRegisterClicked(onSuccess: () -> Unit) {
        validateFullName()
        validateEmail(true)
        validatePassword()

        val state = _uiState.value
        if (state.fullNameError != null || state.emailError != null || state.passwordError != null) {
            return
        }

        viewModelScope.launch {
            authRepository.clearSessionCookies()
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = authRepository.register(state.fullName, state.email, state.password)
            _uiState.value = _uiState.value.copy(isLoading = false)

            if (result.isSuccess) {
                onSuccess()
            } else {
                val errorMessage = result.exceptionOrNull()?.message
                if(errorMessage?.contains("email", ignoreCase = true) == true){
                    _uiState.value = _uiState.value.copy(emailError = errorMessage)
                }else {
                    _uiState.value = _uiState.value.copy(generalError = errorMessage)
                }
            }
        }
    }
}