package com.example.movieroulette.domain.repository

import com.example.movieroulette.domain.model.Genre
import com.example.movieroulette.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getGenres(apiKey: String): List<Genre>
    suspend fun discoverMovies(apiKey: String, genresCsv: String?, page: Int): List<Movie>
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun addFavorite(movie: Movie)
    suspend fun removeFavorite(movie: Movie)
    suspend fun isFavorite(id: Int): Boolean
}
