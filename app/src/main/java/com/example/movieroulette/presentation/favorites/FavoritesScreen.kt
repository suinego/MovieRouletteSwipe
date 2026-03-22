package com.example.movieroulette.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movieroulette.ui.theme.*
import com.example.movieroulette.util.IMAGE_BASE
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(navController: NavController, vm: FavoritesViewModel = koinViewModel()) {
    val favs by vm.favorites.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        Text(
            text       = "Избранное",
            color      = NeonCyan,
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.padding(start = 20.dp, top = 52.dp, bottom = 16.dp)
        )

        if (favs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Здесь пока пусто", color = SubText, fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                modifier            = Modifier.fillMaxSize(),
                contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favs) { m ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkCard)
                            .border(1.dp, NeonCyan.copy(alpha = .15f), RoundedCornerShape(16.dp))
                            .clickable {
                                navController.currentBackStackEntry?.savedStateHandle?.apply {
                                    set("movieId",       m.id)
                                    set("movieTitle",    m.title)
                                    set("moviePoster",   m.posterPath)
                                    set("movieOverview", m.overview)
                                    set("movieRating",   m.voteAverage)
                                }
                                navController.navigate("details")
                            }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model              = m.posterPath?.let { IMAGE_BASE + it },
                            contentDescription = m.title,
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier
                                .size(width = 60.dp, height = 88.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkBg)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text       = m.title ?: "",
                                color      = Color.White,
                                fontSize   = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines   = 2,
                                overflow   = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(4.dp))
                            if (m.voteAverage != null && m.voteAverage > 0.0) {
                                Text(
                                    text     = "★ ${"%.1f".format(m.voteAverage)}",
                                    color    = NeonCyan,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        IconButton(onClick = { vm.remove(m) }) {
                            Icon(
                                imageVector        = Icons.Rounded.Delete,
                                contentDescription = "Удалить",
                                tint               = NeonPink.copy(alpha = .7f),
                                modifier           = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
