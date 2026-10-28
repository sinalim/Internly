package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.components.PrimaryButton
import com.example.internly.ui.components.ScreenHeader
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.FieldBg
import com.example.internly.ui.theme.Grey

@Composable
fun AccountDetailsScreen(
    name: String,
    email: String,
    onBack: () -> Unit,
    onUpdate: () -> Unit,
) {
    val initials = name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ScreenHeader(title = "Account Details", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Blue, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(initials, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(24.dp))
            DetailRow(label = "Full name", value = name)
            Spacer(Modifier.height(12.dp))
            DetailRow(label = "Email", value = email)
            Spacer(Modifier.height(24.dp))
            PrimaryButton(label = "Update Profile", onClick = onUpdate)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FieldBg, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(label, fontSize = 11.sp, color = Grey)
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}