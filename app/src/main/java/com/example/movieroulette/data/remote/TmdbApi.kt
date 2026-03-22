package com.example.movieroulette.data.remote

import com.example.movieroulette.data.remote.dto.DiscoverResponse
import com.example.movieroulette.data.remote.dto.GenreListResponse
import retrofit2.http.GET
import retrofit2.http.Query



interface TmdbApi {
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): GenreListResponse

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("api_key") apiKey: String,
        @Query("with_genres") withGenres: String? = null,
        @Query("page") page: Int = 1
    ): DiscoverResponse
}
