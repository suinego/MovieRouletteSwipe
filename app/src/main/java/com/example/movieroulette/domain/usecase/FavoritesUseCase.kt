package com.example.movieroulette.domain.usecase

import com.example.movieroulette.domain.model.Movie
import com.example.movieroulette.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val repo: MovieRepository) {
    operator fun invoke(): Flow<List<Movie>> = repo.getFavoriteMovies()
}

class AddFavoriteUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke(movie: Movie) = repo.addFavorite(movie)
}

class RemoveFavoriteUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke(movie: Movie) = repo.removeFavorite(movie)
}

class IsFavoriteUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke(id: Int): Boolean = repo.isFavorite(id)
}
