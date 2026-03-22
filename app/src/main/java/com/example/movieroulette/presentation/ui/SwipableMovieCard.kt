package com.example.movieroulette.presentation.ui

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movieroulette.domain.model.Movie
import com.example.movieroulette.ui.theme.*
import com.example.movieroulette.util.IMAGE_BASE
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipableMovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onSwipeRight: (Movie) -> Unit,
    onSwipeLeft: (Movie) -> Unit,
    onTap: (Movie) -> Unit,
    swipeTrigger: Int = 0,
    onSwipeTriggered: () -> Unit = {}
) {
    val offsetX  = remember { Animatable(0f) }
    val offsetY  = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scope    = rememberCoroutineScope()

    val swipeDistThreshold = 220f
    val swipeVelThreshold  = 400f

    val likeAlpha = (offsetX.value / swipeDistThreshold).coerceIn(0f, 1f)
    val nopeAlpha = (-offsetX.value / swipeDistThreshold).coerceIn(0f, 1f)

    fun flyOff(dir: Float) {
        scope.launch {
            launch { offsetX.animateTo(dir * 1800f, tween(280)) }
            launch { rotation.animateTo(dir * 25f, tween(280)) }
        }
        if (dir > 0) onSwipeRight(movie) else onSwipeLeft(movie)
    }

    fun snapBack() {
        scope.launch {
            launch {
                offsetX.animateTo(
                    0f, spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness    = Spring.StiffnessMedium
                    )
                )
            }
            launch {
                offsetY.animateTo(
                    0f, spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness    = Spring.StiffnessMedium
                    )
                )
            }
            launch {
                rotation.animateTo(
                    0f, spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness    = Spring.StiffnessMedium
                    )
                )
            }
        }
    }

    LaunchedEffect(swipeTrigger) {
        if (swipeTrigger != 0) {
            onSwipeTriggered()
            flyOff(swipeTrigger.toFloat())
        }
    }

    Box(
        modifier = modifier.graphicsLayer {
            translationX = offsetX.value
            translationY = offsetY.value
            rotationZ    = rotation.value
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .background(DarkCard)
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(NeonCyan.copy(alpha = .35f), NeonPink.copy(alpha = .35f))
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .pointerInput(movie.id) {
                    val velocityTracker = VelocityTracker()
                    detectDragGestures(
                        onDragStart = { velocityTracker.resetTracking() },
                        onDrag = { change, drag ->
                            change.consume()
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position
                            )
                            scope.launch {
                                offsetX.snapTo(offsetX.value + drag.x)
                                offsetY.snapTo(offsetY.value + drag.y)
                                rotation.snapTo(offsetX.value / 55f)
                            }
                        },
                        onDragEnd = {
                            val vel = velocityTracker.calculateVelocity()
                            val fastSwipe  = abs(vel.x) > swipeVelThreshold
                            val farSwipe   = abs(offsetX.value) > swipeDistThreshold
                            if (fastSwipe || farSwipe) {
                                val dir = if (offsetX.value > 0 || vel.x > 0) 1f else -1f
                                flyOff(dir)
                            } else {
                                snapBack()
                            }
                        }
                    )
                }
        ) {
            val imageUrl = if (movie.posterPath != null) IMAGE_BASE + movie.posterPath else null
            Log.d("CoilImage", "Loading: $imageUrl")
            AsyncImage(
                model              = imageUrl,
                contentDescription = movie.title,
                contentScale       = ContentScale.Crop,
                onError   = { Log.e("CoilImage", "Error loading $imageUrl: ${it.result.throwable}") },
                onSuccess = { Log.d("CoilImage", "Loaded OK: $imageUrl") },
                modifier  = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures { onTap(movie) } }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f    to Color.Transparent,
                            0.5f  to Color.Transparent,
                            1f    to Color(0xF20A0C12)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                if (movie.genreNames.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        movie.genreNames.take(3).forEach { genre ->
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = NeonMagenta.copy(alpha = .18f),
                                tonalElevation = 0.dp
                            ) {
                                Text(
                                    text     = genre,
                                    color    = NeonMagenta,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text       = movie.title,
                    color      = Color.White,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 5.dp)
                ) {
                    if (!movie.releaseDate.isNullOrBlank()) {
                        Text(movie.releaseDate.take(4), color = SubText, fontSize = 13.sp)
                    }
                    if (movie.voteAverage > 0.0) {
                        Text(
                            "★ ${"%.1f".format(movie.voteAverage)}",
                            color      = NeonCyan,
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (movie.voteCount > 0) {
                        Text(
                            "${movie.voteCount / 1000}K",
                            color    = SubText,
                            fontSize = 12.sp
                        )
                    }
                    if (!movie.originalLanguage.isNullOrBlank()) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = NeonCyan.copy(alpha = .15f),
                            tonalElevation = 0.dp
                        ) {
                            Text(
                                text     = movie.originalLanguage.uppercase(),
                                color    = NeonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            if (likeAlpha > 0.05f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(28.dp)
                        .alpha(likeAlpha)
                        .border(3.dp, NeonCyan, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        "ДА",
                        color      = NeonCyan,
                        fontSize   = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            if (nopeAlpha > 0.05f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(28.dp)
                        .alpha(nopeAlpha)
                        .border(3.dp, NeonPink, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        "НЕТ",
                        color      = NeonPink,
                        fontSize   = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
