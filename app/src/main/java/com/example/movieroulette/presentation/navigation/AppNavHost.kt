package com.example.movieroulette.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieroulette.data.local.UserPreferences
import com.example.movieroulette.presentation.auth.AuthScreen
import com.example.movieroulette.presentation.details.DetailsScreen
import com.example.movieroulette.presentation.genres.GenresScreen
import com.example.movieroulette.presentation.main.MainScreen
import com.example.movieroulette.presentation.profile.ProfileScreen
import org.koin.compose.koinInject

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val prefs: UserPreferences = koinInject()

    val start = if (prefs.isLoggedIn) Routes.GENRES else Routes.AUTH

    NavHost(navController = navController, startDestination = start) {

        composable(Routes.AUTH) {
            AuthScreen(navController)
        }

        composable(Routes.GENRES) {
            GenresScreen(navController)
        }

        composable(
            route = Routes.MAIN,
            arguments = listOf(navArgument("genres") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStack ->
            val genresCsv = backStack.arguments?.getString("genres")
            MainScreen(navController, genresCsv = genresCsv)
        }

        composable(Routes.DETAILS) {
            val savedState = navController.previousBackStackEntry?.savedStateHandle
            val genresCsv: String? = savedState?.get("movieGenres")
            DetailsScreen(
                movieId      = savedState?.get("movieId"),
                title        = savedState?.get("movieTitle"),
                posterPath   = savedState?.get("moviePoster"),
                overview     = savedState?.get("movieOverview"),
                rating       = savedState?.get("movieRating"),
                releaseDate  = savedState?.get("movieReleaseDate"),
                voteCount    = savedState?.get("movieVoteCount"),
                language     = savedState?.get("movieLanguage"),
                genreNames   = genresCsv?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController)
        }
    }
}
