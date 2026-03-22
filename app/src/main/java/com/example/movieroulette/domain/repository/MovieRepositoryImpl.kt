package com.example.movieroulette.domain.repository

import com.example.movieroulette.data.local.FavoriteMovie
import com.example.movieroulette.data.local.FavoriteMovieDao
import com.example.movieroulette.data.local.GenreCache
import com.example.movieroulette.data.remote.TmdbApi
import com.example.movieroulette.data.remote.dto.MovieDto
import com.example.movieroulette.domain.model.Genre
import com.example.movieroulette.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val api: TmdbApi,
    private val favoriteDao: FavoriteMovieDao,
    private val genreCache: GenreCache
) : MovieRepository {

    private fun toDomain(dto: MovieDto) = Movie(
        id               = dto.id,
        title            = dto.title.orEmpty(),
        overview         = dto.overview.orEmpty(),
        posterPath       = dto.posterPath,
        voteAverage      = dto.voteAverage ?: 0.0,
        voteCount        = dto.voteCount ?: 0,
        releaseDate      = dto.releaseDate,
        genreNames       = genreCache.resolve(dto.genreIds ?: emptyList()),
        originalLanguage = dto.originalLanguage,
        popularity       = dto.popularity ?: 0.0
    )

    override suspend fun getGenres(apiKey: String): List<Genre> {
        val resp = api.getGenres(apiKey)
        val genres = resp.genres.map { Genre(id = it.id, name = it.name) }
        genreCache.populate(genres.associate { it.id to it.name })
        return genres
    }

    override suspend fun discoverMovies(apiKey: String, genresCsv: String?, page: Int): List<Movie> {
        val resp = api.discoverMovies(apiKey, genresCsv, page)
        return resp.results.map { toDomain(it) }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        favoriteDao.getAllFavorites().map { list ->
            list.map {
                Movie(
                    id               = it.id,
                    title            = it.title.orEmpty(),
                    overview         = it.overview.orEmpty(),
                    posterPath       = it.posterPath,
                    voteAverage      = it.voteAverage ?: 0.0,
                    voteCount        = 0,
                    releaseDate      = it.releaseDate,
                    genreNames       = emptyList(),
                    originalLanguage = null,
                    popularity       = 0.0
                )
            }
        }

    override suspend fun addFavorite(movie: Movie) {
        favoriteDao.insert(
            FavoriteMovie(
                id          = movie.id,
                title       = movie.title,
                overview    = movie.overview,
                posterPath  = movie.posterPath,
                voteAverage = movie.voteAverage,
                releaseDate = movie.releaseDate
            )
        )
    }

    override suspend fun removeFavorite(movie: Movie) {
        favoriteDao.delete(
            FavoriteMovie(
                id          = movie.id,
                title       = movie.title,
                overview    = movie.overview,
                posterPath  = movie.posterPath,
                voteAverage = movie.voteAverage,
                releaseDate = movie.releaseDate
            )
        )
    }

    override suspend fun isFavorite(id: Int) = favoriteDao.isFavorite(id)
}
