package com.example.movieroulette.presentation.genres

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movieroulette.presentation.navigation.Routes
import com.example.movieroulette.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun GenresScreen(navController: NavController, vm: GenresViewModel = koinViewModel()) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text       = "Выберите жанры",
            color      = NeonCyan,
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.padding(top = 52.dp, bottom = 4.dp)
        )
        Text(
            text     = "Можно пропустить и тогда Ты увидишь все!",
            color    = SubText,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        when {
            state.loading -> {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator(color = NeonCyan)
                }
            }
            state.error != null -> {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Ошибка: ${state.error}", color = NeonPink)
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns             = GridCells.Fixed(2),
                    modifier            = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.genres) { genre ->
                        val selected = state.selectedIds.contains(genre.id)
                        Box(
                            modifier = Modifier
                                .height(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (selected) NeonCyan.copy(alpha = .15f) else DarkCard)
                                .border(
                                    width = 1.5.dp,
                                    color = if (selected) NeonCyan else NeonCyan.copy(.2f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { vm.toggleSelection(genre.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = genre.name,
                                color      = if (selected) NeonCyan else OnDark,
                                fontSize   = 14.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { vm.clear() },
                modifier = Modifier.weight(1f).height(50.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkCard,
                    contentColor   = SubText
                )
            ) { Text("Очистить") }

            Button(
                onClick = {
                    val csv = state.selectedIds.joinToString(",")
                    navController.navigate(Routes.main(csv.ifBlank { null }))
                },
                modifier = Modifier.weight(1f).height(50.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan,
                    contentColor   = DarkBg
                )
            ) {
                Text("Поехали!", fontWeight = FontWeight.Bold)
            }
        }
    }
}
