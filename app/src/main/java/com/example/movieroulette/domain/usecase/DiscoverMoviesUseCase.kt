package com.example.movieroulette.domain.usecase

import com.example.movieroulette.domain.model.Movie
import com.example.movieroulette.domain.repository.MovieRepository

class DiscoverMoviesUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke(apiKey: String, genresCsv: String?, page: Int): List<Movie> =
        repo.discoverMovies(apiKey, genresCsv, page)
}
