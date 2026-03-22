package com.example.movieroulette.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieroulette.domain.model.Movie
import com.example.movieroulette.domain.usecase.AddFavoriteUseCase
import com.example.movieroulette.domain.usecase.IsFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DetailsUiState(val movie: Movie? = null, val isFavorite: Boolean = false)

class DetailsViewModel(
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState

    fun setMovie(movie: Movie) {
        _uiState.value = _uiState.value.copy(movie = movie)
        viewModelScope.launch {
            val fav = isFavoriteUseCase(movie.id)
            _uiState.value = _uiState.value.copy(isFavorite = fav)
        }
    }

    fun addToFavorites() {
        viewModelScope.launch {
            _uiState.value.movie?.let {
                addFavoriteUseCase(it)
                _uiState.value = _uiState.value.copy(isFavorite = true)
            }
        }
    }
}
