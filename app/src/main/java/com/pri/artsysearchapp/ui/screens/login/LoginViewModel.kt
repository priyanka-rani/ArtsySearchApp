package com.pri.artsysearchapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pri.artsysearchapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.clearSessionCookies()
            val result = repository.login(email, password)
            if (result.isSuccess) {
                onSuccess()
            } else {

                onError(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

}