package com.example.movieroulette.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieroulette.BuildConfig
import com.example.movieroulette.domain.model.Movie
import com.example.movieroulette.domain.usecase.AddFavoriteUseCase
import com.example.movieroulette.domain.usecase.DiscoverMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val loading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val page: Int = 1,
    val error: String? = null
)

class MainViewModel(
    private val discoverMoviesUseCase: DiscoverMoviesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState(loading = true))
    val uiState: StateFlow<MainUiState> = _uiState

    private val apiKey = BuildConfig.TMDB_API_KEY
    private var isLoading = false
    private var genresCsv: String? = null

    fun initialize(genres: String?) {
        genresCsv = genres?.ifBlank { null }
        _uiState.value = MainUiState(loading = true)
        isLoading = false
        loadNextPage()
    }

    fun loadNextPage() {
        val currentPage = _uiState.value.page
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                val movies = discoverMoviesUseCase(apiKey, genresCsv, currentPage)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    movies  = _uiState.value.movies + movies,
                    page    = currentPage + 1
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(loading = false, error = e.message ?: "Unknown")
            }
            isLoading = false
        }
    }

    fun like(movie: Movie) {
        viewModelScope.launch { addFavoriteUseCase(movie) }
    }

    fun popTop(): Movie? {
        val list = _uiState.value.movies
        return if (list.isEmpty()) null else {
            val top = list.first()
            _uiState.value = _uiState.value.copy(movies = list.drop(1))
            top
        }
    }

    fun clearError() { _uiState.value = _uiState.value.copy(error = null) }
}
