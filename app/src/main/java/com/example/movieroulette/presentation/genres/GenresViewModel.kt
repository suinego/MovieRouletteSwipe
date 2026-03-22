package com.example.movieroulette.presentation.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieroulette.BuildConfig
import com.example.movieroulette.domain.model.Genre
import com.example.movieroulette.domain.usecase.GetGenresUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class GenresUiState(
    val loading: Boolean = false,
    val genres: List<Genre> = emptyList(),
    val error: String? = null,
    val selectedIds: Set<Int> = emptySet()
)

class GenresViewModel(private val getGenresUseCase: GetGenresUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(GenresUiState(loading = true))
    val uiState: StateFlow<GenresUiState> = _uiState

    init { loadGenres() }

    private fun loadGenres() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val apiKey = BuildConfig.TMDB_API_KEY
                val genres = getGenresUseCase(apiKey)
                _uiState.value = _uiState.value.copy(loading = false, genres = genres)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(loading = false, error = e.message ?: "Unknown")
            }
        }
    }

    fun toggleSelection(id: Int) {
        val current = _uiState.value.selectedIds.toMutableSet()
        if (!current.add(id)) current.remove(id)
        _uiState.value = _uiState.value.copy(selectedIds = current)
    }

    fun clear() { _uiState.value = _uiState.value.copy(selectedIds = emptySet()) }
}
