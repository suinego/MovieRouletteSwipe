package com.example.movieroulette.data.remote.dto

import com.squareup.moshi.Json

data class MovieDto(
    val id: Int,
    @Json(name = "title")             val title: String?,
    @Json(name = "overview")          val overview: String?,
    @Json(name = "poster_path")       val posterPath: String?,
    @Json(name = "vote_average")      val voteAverage: Double?,
    @Json(name = "vote_count")        val voteCount: Int?,
    @Json(name = "release_date")      val releaseDate: String?,
    @Json(name = "genre_ids")         val genreIds: List<Int>?,
    @Json(name = "original_language") val originalLanguage: String?,
    @Json(name = "popularity")        val popularity: Double?
)

data class DiscoverResponse(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)
