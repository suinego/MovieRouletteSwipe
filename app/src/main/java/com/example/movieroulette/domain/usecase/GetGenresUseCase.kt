package com.example.movieroulette.domain.usecase

import com.example.movieroulette.domain.model.Genre
import com.example.movieroulette.domain.repository.MovieRepository

class GetGenresUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke(apiKey: String): List<Genre> = repo.getGenres(apiKey)
}
