package com.example.movieroulette.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.movieroulette.data.local.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class AuthUiState(
    val name: String  = "",
    val email: String = "",
    val nameError: String? = null,
    val isRegister: Boolean = true
)

class AuthViewModel(private val prefs: UserPreferences) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun setName(v: String)  { _state.value = _state.value.copy(name = v, nameError = null) }
    fun setEmail(v: String) { _state.value = _state.value.copy(email = v) }
    fun setTab(register: Boolean) { _state.value = _state.value.copy(isRegister = register, nameError = null) }

    fun submit(): Boolean {
        val s = _state.value
        if (s.name.isBlank()) {
            _state.value = s.copy(nameError = "Введите имя")
            return false
        }
        prefs.name  = s.name.trim()
        prefs.email = s.email.trim()
        prefs.isLoggedIn = true
        return true
    }
}
