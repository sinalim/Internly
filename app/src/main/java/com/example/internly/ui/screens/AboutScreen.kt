package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.components.Logo
import com.example.internly.ui.components.ScreenHeader
import com.example.internly.ui.theme.Grey

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ScreenHeader(title = "About", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(48.dp))
            Logo(size = 56.dp)
            Spacer(Modifier.height(12.dp))
            Text("Version 1.0", fontSize = 13.sp, color = Grey)
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Internly helps you track every internship application, never miss a follow-up or deadline, and prepare for interviews with AI.",
                fontSize = 14.sp,
                color = Grey,
                textAlign = TextAlign.Center,
            )
        }
    }
}
