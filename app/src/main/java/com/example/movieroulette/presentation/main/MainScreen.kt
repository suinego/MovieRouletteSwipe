package com.example.movieroulette.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movieroulette.data.local.UserPreferences
import com.example.movieroulette.presentation.navigation.Routes
import com.example.movieroulette.presentation.ui.SwipableMovieCard
import com.example.movieroulette.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    navController: NavController,
    genresCsv: String? = null,
    vm: MainViewModel = koinViewModel()
) {
    val state by vm.uiState.collectAsState()
    val prefs: UserPreferences = koinInject()

    LaunchedEffect(Unit) { vm.initialize(genresCsv) }

    var swipeTrigger by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = "Movie Roulette",
                color      = NeonCyan,
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(listOf(NeonCyan, NeonMagenta)),
                        shape = CircleShape
                    )
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(NeonCyan.copy(.2f), DarkCard)))
                    .clip(CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { navController.navigate(Routes.PROFILE) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = prefs.initials,
                    color      = NeonCyan,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when {
                state.movies.isEmpty() && state.loading -> {
                    CircularProgressIndicator(
                        color    = NeonCyan,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.movies.isEmpty() -> {
                    Column(
                        modifier          = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Фильмы закончились", color = SubText, fontSize = 16.sp)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { vm.loadNextPage() },
                            colors  = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan,
                                contentColor   = DarkBg
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("Загрузить ещё", fontWeight = FontWeight.Bold) }
                    }
                }
                else -> {
                    val stack = state.movies.take(3)

                    stack.reversed().forEachIndexed { idx, movie ->
                        val isTop = idx == stack.size - 1
                        if (!isTop) {
                            val offset = ((stack.size - 1 - idx) * 7).dp
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = offset, end = offset, bottom = offset),
                                shape  = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkCard)
                            ) {}
                        }
                    }

                    val topMovie = stack.first()
                    key(topMovie.id) {
                        SwipableMovieCard(
                            movie            = topMovie,
                            modifier         = Modifier.fillMaxSize(),
                            swipeTrigger     = swipeTrigger,
                            onSwipeTriggered = { swipeTrigger = 0 },
                            onSwipeLeft      = { vm.popTop() },
                            onSwipeRight     = { vm.like(it); vm.popTop() },
                            onTap            = {
                                navController.currentBackStackEntry?.savedStateHandle?.apply {
                                    set("movieId",         topMovie.id)
                                    set("movieTitle",      topMovie.title)
                                    set("moviePoster",     topMovie.posterPath)
                                    set("movieOverview",   topMovie.overview)
                                    set("movieRating",     topMovie.voteAverage)
                                    set("movieReleaseDate",topMovie.releaseDate)
                                    set("movieVoteCount",  topMovie.voteCount)
                                    set("movieLanguage",   topMovie.originalLanguage)
                                    set("movieGenres",     topMovie.genreNames.joinToString(","))
                                }
                                navController.navigate(Routes.DETAILS)
                            }
                        )
                    }
                }
            }
        }

        if (state.movies.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick  = { swipeTrigger = -1 },
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(NeonPink.copy(alpha = .18f), Color.Transparent)
                            ),
                            CircleShape
                        )
                        .border(2.dp, NeonPink, CircleShape)
                ) {
                    Icon(
                        imageVector        = Icons.Rounded.Close,
                        contentDescription = "Пропустить",
                        tint               = NeonPink,
                        modifier           = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick  = { swipeTrigger = 1 },
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(NeonCyan.copy(alpha = .18f), Color.Transparent)
                            ),
                            CircleShape
                        )
                        .border(2.dp, NeonCyan, CircleShape)
                ) {
                    Icon(
                        imageVector        = Icons.Rounded.Favorite,
                        contentDescription = "Лайк",
                        tint               = NeonCyan,
                        modifier           = Modifier.size(30.dp)
                    )
                }
            }
        } else {
            Spacer(Modifier.height(108.dp))
        }
    }

    LaunchedEffect(state.movies.size) {
        if (!state.loading && state.movies.isNotEmpty() && state.movies.size < 6) {
            vm.loadNextPage()
        }
    }
}
