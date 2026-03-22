package com.example.movieroulette.di

import com.example.movieroulette.data.local.UserPreferences
import com.example.movieroulette.domain.usecase.*
import com.example.movieroulette.presentation.auth.AuthViewModel
import com.example.movieroulette.presentation.details.DetailsViewModel
import com.example.movieroulette.presentation.genres.GenresViewModel
import com.example.movieroulette.presentation.main.MainViewModel
import com.example.movieroulette.presentation.profile.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { UserPreferences(androidContext()) }

    single { GetGenresUseCase(get()) }
    single { DiscoverMoviesUseCase(get()) }
    single { GetFavoritesUseCase(get()) }
    single { AddFavoriteUseCase(get()) }
    single { RemoveFavoriteUseCase(get()) }
    single { IsFavoriteUseCase(get()) }

    viewModel { AuthViewModel(get()) }
    viewModel { GenresViewModel(get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { DetailsViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
}
