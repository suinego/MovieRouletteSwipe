package com.example.movieroulette.di

import com.example.movieroulette.data.local.GenreCache
import com.example.movieroulette.domain.repository.MovieRepositoryImpl
import com.example.movieroulette.domain.repository.MovieRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single { GenreCache() }
    single { MovieRepositoryImpl(get(), get(), get()) } bind MovieRepository::class
}
