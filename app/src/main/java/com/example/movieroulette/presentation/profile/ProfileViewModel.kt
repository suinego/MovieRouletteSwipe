package com.example.movieroulette.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieroulette.data.local.UserPreferences
import com.example.movieroulette.domain.model.Movie
import com.example.movieroulette.domain.usecase.GetFavoritesUseCase
import com.example.movieroulette.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    val prefs: UserPreferences,
    getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    val favorites = getFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFavorite(movie: Movie) {
        viewModelScope.launch { removeFavoriteUseCase(movie) }
    }

    fun logout(onDone: () -> Unit) {
        prefs.logout()
        onDone()
    }
}
