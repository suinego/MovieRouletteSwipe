package com.example.movieroulette.di

import androidx.room.Room
import com.example.movieroulette.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.jvm.java
val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().favoriteMovieDao() }
}
