package com.example.movieroulette.data.local

class GenreCache {
    private var map: Map<Int, String> = emptyMap()

    fun populate(genres: Map<Int, String>) {
        map = genres
    }

    fun resolve(ids: List<Int>): List<String> = ids.mapNotNull { map[it] }

    val isEmpty get() = map.isEmpty()
}
