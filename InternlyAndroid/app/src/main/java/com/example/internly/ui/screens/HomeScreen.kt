package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.components.AttentionCard
import com.example.internly.ui.components.StatChip
import com.example.internly.ui.theme.AppBg
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Green
import com.example.internly.ui.theme.Navy
import com.example.internly.ui.theme.Orange
import com.example.internly.ui.theme.SkyBlue

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "A little progress goes a long way",
                modifier = Modifier.weight(1f),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy,
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.NotificationsNone, contentDescription = "Notifications", tint = Navy)
            }
        }
        Spacer(Modifier.height(16.dp))

        // Progress card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(Blue, Navy)),
                    RoundedCornerShape(16.dp),
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 0.75f },
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.25f),
                    strokeWidth = 6.dp,
                )
                Text("3/4", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Applications", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("3 active · 1 closed", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(14.dp))

        // Stat chips
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatChip("4", "Applied", Modifier.weight(1f))
            StatChip("2", "Interview", Modifier.weight(1f))
            StatChip("1", "Offer", Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))

        // Needs attention
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Needs your attention", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("See All", color = Blue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(10.dp))

        AttentionCard(
            company = "TechCorp",
            tag = "Interview · 2 days",
            note = "Prepare for your interview",
            color = Orange,
            icon = Icons.Filled.PriorityHigh,
        )
        AttentionCard(
            company = "BrightPath",
            tag = "Follow-Up · 3 days",
            note = "Send a follow up email",
            color = SkyBlue,
            icon = Icons.Filled.MailOutline,
        )
        AttentionCard(
            company = "SkyLabs",
            tag = "Assessment · 5 days",
            note = "Complete technical test",
            color = Green,
            icon = Icons.Filled.Assignment,
        )
    }
}
