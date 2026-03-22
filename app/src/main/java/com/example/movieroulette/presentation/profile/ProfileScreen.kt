package com.example.movieroulette.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movieroulette.presentation.navigation.Routes
import com.example.movieroulette.ui.theme.*
import com.example.movieroulette.util.IMAGE_BASE
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(navController: NavController, vm: ProfileViewModel = koinViewModel()) {
    val favorites by vm.favorites.collectAsState()
    val prefs = vm.prefs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(DarkSurface, DarkBg))
                )
                .padding(top = 52.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
        ) {
            TextButton(
                onClick = {
                    vm.logout {
                        navController.navigate(Routes.AUTH) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("Выйти", color = SubText, fontSize = 13.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .border(
                            width = 2.5.dp,
                            brush = Brush.linearGradient(listOf(NeonCyan, NeonMagenta)),
                            shape = CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(listOf(NeonCyan.copy(.2f), DarkCard))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = prefs.initials,
                        color      = NeonCyan,
                        fontSize   = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text       = prefs.name,
                    color      = Color.White,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                if (prefs.email.isNotBlank()) {
                    Text(
                        text     = prefs.email,
                        color    = SubText,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkCard)
                        .border(1.dp, NeonCyan.copy(.3f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text       = "${favorites.size} понравилось",
                            color      = NeonCyan,
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text      = "Пока нет понравившихся фильмов",
                        color     = SubText,
                        fontSize  = 15.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text     = "Свайпай вправо, чтобы добавить",
                        color    = SubText.copy(.6f),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            Text(
                text     = "Понравившиеся",
                color    = OnDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            LazyVerticalGrid(
                columns             = GridCells.Fixed(3),
                modifier            = Modifier.fillMaxSize(),
                contentPadding      = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites) { movie ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(0.67f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkCard)
                            .clickable {
                                navController.currentBackStackEntry?.savedStateHandle?.apply {
                                    set("movieId",       movie.id)
                                    set("movieTitle",    movie.title)
                                    set("moviePoster",   movie.posterPath)
                                    set("movieOverview", movie.overview)
                                    set("movieRating",   movie.voteAverage)
                                }
                                navController.navigate(Routes.DETAILS)
                            }
                    ) {
                        AsyncImage(
                            model              = movie.posterPath?.let { IMAGE_BASE + it },
                            contentDescription = movie.title,
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color(0xDD0A0C12))
                                    )
                                )
                                .padding(6.dp)
                        ) {
                            Text(
                                text       = movie.title,
                                color      = Color.White,
                                fontSize   = 10.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines   = 2,
                                overflow   = TextOverflow.Ellipsis
                            )
                        }

                        IconButton(
                            onClick  = { vm.removeFavorite(movie) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(28.dp)
                                .padding(2.dp)
                                .background(Color(0x99000000), CircleShape)
                        ) {
                            Icon(
                                imageVector        = Icons.Rounded.Close,
                                contentDescription = "Удалить",
                                tint               = NeonPink,
                                modifier           = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
