package com.example.movieroulette.presentation.navigation

object Routes {
    const val AUTH      = "auth"
    const val GENRES    = "genres"
    const val MAIN      = "main?genres={genres}"
    const val DETAILS   = "details"
    const val PROFILE   = "profile"

    fun main(genresCsv: String?) =
        if (genresCsv.isNullOrBlank()) "main" else "main?genres=$genresCsv"
}
