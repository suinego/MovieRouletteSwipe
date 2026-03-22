package com.example.movieroulette.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieroulette.domain.model.Movie
import com.example.movieroulette.domain.usecase.GetFavoritesUseCase
import com.example.movieroulette.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {
    val favorites = getFavoritesUseCase().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun remove(movie: Movie) {
        viewModelScope.launch {
            removeFavoriteUseCase(movie)
        }
    }
}
