package com.example.movieroulette.data.remote.dto

data class GenreDto(val id: Int, val name: String)
data class GenreListResponse(val genres: List<GenreDto>)
