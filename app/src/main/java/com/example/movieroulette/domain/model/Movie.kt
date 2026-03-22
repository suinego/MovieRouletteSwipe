package com.example.movieroulette.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String?,
    val genreNames: List<String>,
    val originalLanguage: String?,
    val popularity: Double
) : Parcelable
