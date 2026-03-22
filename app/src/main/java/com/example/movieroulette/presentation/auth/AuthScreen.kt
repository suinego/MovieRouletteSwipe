package com.example.movieroulette.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movieroulette.presentation.navigation.Routes
import com.example.movieroulette.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(navController: NavController, vm: AuthViewModel = koinViewModel()) {
    val state by vm.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text       = "🎬",
                fontSize   = 56.sp,
                modifier   = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text       = "Movie Roulette",
                color      = NeonCyan,
                fontSize   = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text     = "Просто свайпай. Ничего лишнего",
                color    = SubText,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkCard)
                    .padding(4.dp)
            ) {
                listOf(true to "Регистрация", false to "Войти").forEach { (isReg, label) ->
                    val selected = state.isRegister == isReg
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selected)
                                    Brush.horizontalGradient(listOf(NeonCyan.copy(.2f), NeonMagenta.copy(.2f)))
                                else
                                    Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                            )
                            .border(
                                width = if (selected) 1.dp else 0.dp,
                                color = if (selected) NeonCyan.copy(.5f) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { vm.setTab(isReg) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = label,
                            color      = if (selected) NeonCyan else SubText,
                            fontSize   = 14.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Name field
            NeoTextField(
                value         = state.name,
                onValueChange = vm::setName,
                label         = "Имя",
                placeholder   = "Ваше имя",
                error         = state.nameError,
                imeAction     = ImeAction.Next
            )

            Spacer(Modifier.height(14.dp))

            NeoTextField(
                value         = state.email,
                onValueChange = vm::setEmail,
                label         = "Email",
                placeholder   = "ваша@почта.com",
                keyboardType  = KeyboardType.Email,
                imeAction     = ImeAction.Done
            )

            Spacer(Modifier.height(32.dp))

            // Submit button
            Button(
                onClick = {
                    if (vm.submit()) {
                        navController.navigate(Routes.GENRES) {
                            popUpTo(Routes.AUTH) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan,
                    contentColor   = DarkBg
                )
            ) {
                Text(
                    text       = if (state.isRegister) "Создать профиль" else "Войти",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NeoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text     = label,
            color    = SubText,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, color = SubText.copy(.5f)) },
            singleLine    = true,
            isError       = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            modifier      = Modifier.fillMaxWidth(),
            shape         = RoundedCornerShape(14.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = NeonCyan,
                unfocusedBorderColor = NeonCyan.copy(.25f),
                errorBorderColor     = NeonPink,
                focusedTextColor     = Color.White,
                unfocusedTextColor   = OnDark,
                cursorColor          = NeonCyan,
                focusedContainerColor   = DarkCard,
                unfocusedContainerColor = DarkCard,
                errorContainerColor     = DarkCard
            )
        )
        AnimatedVisibility(visible = error != null, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text     = error ?: "",
                color    = NeonPink,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
