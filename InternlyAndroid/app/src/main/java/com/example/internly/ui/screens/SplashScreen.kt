package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.internly.ui.components.Logo
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val finished by rememberUpdatedState(onFinished)
    LaunchedEffect(Unit) {
        delay(2000)
        finished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF3B2FA0), Color(0xFF1B1F8A), Color(0xFF0E1260))
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Logo(size = 56.dp, color = Color.White)
    }
}
