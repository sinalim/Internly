package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.ApplicationStore
import com.example.internly.data.JobApplication
import com.example.internly.ui.components.AppLogo
import com.example.internly.ui.components.StatChip
import com.example.internly.ui.theme.AppBg
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Green
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy
import com.example.internly.ui.theme.Orange
import com.example.internly.ui.theme.SkyBlue

private fun daysSince(millis: Long): Int =
    ((System.currentTimeMillis() - millis) / 86_400_000L).toInt().coerceAtLeast(0)

private fun agoText(millis: Long): String {
    val d = daysSince(millis)
    return when (d) {
        0 -> "Today"
        1 -> "1 day ago"
        else -> "$d days ago"
    }
}

@Composable
fun HomeScreen(onPrepForInterview: () -> Unit = {}) {
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var showAll by remember { mutableStateOf(false) }

    if (selectedId != null) {
        ApplicationSummaryScreen(
            id = selectedId!!,
            onBack = { selectedId = null },
        )
        return
    }

    val apps = ApplicationStore.applications
    val total = apps.size
    val closed = apps.count { it.status == "Offer" || it.status == "Rejected" }
    val active = total - closed
    val progress = if (total == 0) 0f else active.toFloat() / total

    val attention: List<JobApplication> = apps
        .filter { it.status == "Interview" || it.status == "Assessment" || it.status == "Applied" }
        .sortedBy {
            when (it.status) {
                "Interview" -> 0
                "Assessment" -> 1
                else -> 2
            }
        }
    val shownAttention = if (showAll) attention else attention.take(3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
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
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.25f),
                    strokeWidth = 6.dp,
                )
                Text("$active/$total", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Applications", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("$active active · $closed closed", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatChip("$total", "Applied", Modifier.weight(1f))
            StatChip("${ApplicationStore.count("Interview")}", "Interview", Modifier.weight(1f))
            StatChip("${ApplicationStore.count("Offer")}", "Offer", Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Needs your attention", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            if (attention.size > 3) {
                Text(
                    if (showAll) "Show less" else "See All",
                    color = Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { showAll = !showAll },
                )
            }
        }
        Spacer(Modifier.height(10.dp))

        if (shownAttention.isEmpty()) {
            Text(
                "Nothing needs your attention right now.",
                fontSize = 13.sp,
                color = Color(0xFF6B7280),
            )
        }

        shownAttention.forEach { app ->
            val (tagColor, tagLabel, note) = when (app.status) {
                "Interview" -> Triple(Orange, "Interview", "Prepare for your interview")
                "Assessment" -> Triple(Green, "Assessment", "Complete technical test")
                else -> Triple(SkyBlue, "Follow-Up", "Send a follow up email")
            }
            AttentionRow(
                app = app,
                tagColor = tagColor,
                tag = "$tagLabel · ${agoText(app.dateMillis)}",
                note = note,
                onClick = { selectedId = app.id },
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun AttentionRow(
    app: JobApplication,
    tagColor: Color,
    tag: String,
    note: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppLogo(app.company, size = 48.dp)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(app.company, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            Spacer(Modifier.height(4.dp))
            Text(tag, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = tagColor)
            Spacer(Modifier.height(4.dp))
            Text(note, fontSize = 13.sp, color = Grey)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Grey)
    }
}