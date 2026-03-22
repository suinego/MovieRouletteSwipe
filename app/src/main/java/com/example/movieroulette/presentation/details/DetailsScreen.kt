package com.example.movieroulette.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movieroulette.ui.theme.*
import com.example.movieroulette.util.IMAGE_BASE

@Composable
fun DetailsScreen(
    movieId: Int?,
    title: String?,
    posterPath: String?,
    overview: String?,
    rating: Double?,
    releaseDate: String?,
    voteCount: Int? = null,
    language: String? = null,
    genreNames: List<String> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(460.dp)
        ) {
            posterPath?.let {
                AsyncImage(
                    model              = IMAGE_BASE + it,
                    contentDescription = title,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f    to Color.Transparent,
                            0.65f to Color.Transparent,
                            1f    to DarkBg
                        )
                    )
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            Text(
                text       = title ?: "",
                color      = Color.White,
                fontSize   = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!releaseDate.isNullOrBlank()) {
                    Text(
                        text     = releaseDate.take(4),
                        color    = SubText,
                        fontSize = 14.sp
                    )
                }
                if (rating != null && rating > 0.0) {
                    Text(
                        text       = "★ ${"%.1f".format(rating)}",
                        color      = NeonCyan,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (voteCount != null && voteCount > 0) {
                    Text(
                        text     = "${voteCount / 1000}K оценок",
                        color    = SubText,
                        fontSize = 13.sp
                    )
                }
                if (!language.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = NeonCyan.copy(alpha = .15f),
                        tonalElevation = 0.dp
                    ) {
                        Text(
                            text     = language.uppercase(),
                            color    = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            if (genreNames.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    genreNames.forEach { genre ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = NeonMagenta.copy(alpha = .15f),
                            tonalElevation = 0.dp
                        ) {
                            Text(
                                text     = genre,
                                color    = NeonMagenta,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (!overview.isNullOrBlank()) {
                Text(
                    text       = "Описание",
                    color      = NeonMagenta,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text     = overview,
                    color    = OnDark,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
