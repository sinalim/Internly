package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.ApplicationStore
import com.example.internly.data.JobApplication
import com.example.internly.ui.components.AppLogo
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Green
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy

private val roadmapStages = listOf("Applied", "Assessment", "Interview", "Offer")
private val LineGrey = Color(0xFFCBD2F0)
private val CardLine = Color(0xFFE5E7EB)

private fun stageCircleColor(index: Int): Color = when (index) {
    0 -> Navy
    3 -> Green
    else -> Blue
}

private fun daysAgoText(millis: Long): String {
    val d = ((System.currentTimeMillis() - millis) / 86_400_000L).toInt().coerceAtLeast(0)
    return when (d) {
        0 -> "Today"
        1 -> "1 day ago"
        else -> "$d days ago"
    }
}

@Composable
fun RoadmapScreen(onPrepForInterview: () -> Unit = {}) {
    var selectedId by remember { mutableStateOf<Int?>(null) }

    if (selectedId != null) {
        ApplicationSummaryScreen(
            id = selectedId!!,
            onBack = { selectedId = null },
        )
        return
    }

    val apps = ApplicationStore.applications

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 16.dp),
    ) {
        Text(
            text = "Your Roadmap",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Navy,
        )
        Spacer(Modifier.height(24.dp))

        roadmapStages.forEachIndexed { index, stage ->
            val inStage = apps.filter { it.status == stage }
            val isLast = index == roadmapStages.lastIndex
            val countText = if (inStage.size == 1) "1 application" else "${inStage.size} applications"

            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                // Number circle and the line down to the next stage
                Column(
                    modifier = Modifier.width(40.dp).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier.size(40.dp).background(stageCircleColor(index), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("${index + 1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .weight(1f)
                                .background(LineGrey),
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f).padding(bottom = if (isLast) 0.dp else 24.dp)) {
                    Text(stage, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Text(countText, fontSize = 13.sp, color = Grey)
                    Spacer(Modifier.height(10.dp))

                    if (inStage.isEmpty()) {
                        Text("Nothing at this stage yet", fontSize = 13.sp, color = Grey)
                    } else {
                        inStage.forEach { app ->
                            RoadmapCard(app = app, onClick = { selectedId = app.id })
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoadmapCard(app: JobApplication, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, CardLine, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppLogo(app.company, size = 42.dp)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(app.company, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            Spacer(Modifier.height(2.dp))
            Text("${app.status} · ${daysAgoText(app.dateMillis)}", fontSize = 12.sp, color = Grey)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Grey)
    }
}