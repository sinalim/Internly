package com.example.internly.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.ApplicationStore
import com.example.internly.ui.components.AppLogo
import com.example.internly.ui.theme.AppBg
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy

private val SummaryBorder = Color(0xFFE5E7EB)
private val summaryStages = listOf("Applied", "Assessment", "Interview", "Offer")

/** Read-only page about one application. Nothing on it is clickable except Back. */
@Composable
fun ApplicationSummaryScreen(id: Int, onBack: () -> Unit) {
    BackHandler { onBack() }

    val app = ApplicationStore.byId(id)
    if (app == null) {
        onBack()
        return
    }
    val current = summaryStages.indexOf(app.status)

    Column(modifier = Modifier.fillMaxSize().background(AppBg)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Blue,
                modifier = Modifier.size(24.dp).clickable { onBack() },
            )
            Spacer(Modifier.width(10.dp))
            Text("Your application", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Blue)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            // Header card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Blue, Navy)), RoundedCornerShape(20.dp))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppLogo(app.company, size = 64.dp)
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(app.company, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text(app.role, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                    ) {
                        Text(app.status, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(22.dp))

            // Stage tracker
            Text("Progress", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Navy)
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, SummaryBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 18.dp, horizontal = 8.dp),
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    summaryStages.forEachIndexed { index, stage ->
                        val reached = current >= 0 && index <= current
                        val isCurrent = index == current
                        val leftLit = current >= 0 && index <= current
                        val rightLit = current >= 0 && index < current
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            // Fixed-height row so every circle and line sits on the same level
                            Row(
                                modifier = Modifier.fillMaxWidth().height(40.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(3.dp)
                                        .background(
                                            when {
                                                index == 0 -> Color.Transparent
                                                leftLit -> Blue
                                                else -> SummaryBorder
                                            },
                                        ),
                                )
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            if (isCurrent) Blue.copy(alpha = 0.2f) else Color.Transparent,
                                            CircleShape,
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .background(if (reached) Blue else Color(0xFFF3F4F6), CircleShape)
                                            .border(1.5.dp, if (reached) Blue else SummaryBorder, CircleShape),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        if (reached && !isCurrent) {
                                            Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        } else {
                                            Text(
                                                "${index + 1}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (reached) Color.White else Grey,
                                            )
                                        }
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(3.dp)
                                        .background(
                                            when {
                                                index == summaryStages.lastIndex -> Color.Transparent
                                                rightLit -> Blue
                                                else -> SummaryBorder
                                            },
                                        ),
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                stage,
                                fontSize = 11.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (reached) Navy else Grey,
                            )
                        }
                    }
                }
                if (current < 0) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "This application is marked as ${app.status}.",
                        fontSize = 12.sp,
                        color = Grey,
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            }
            Spacer(Modifier.height(22.dp))

            // Details
            Text("Details", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Navy)
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, SummaryBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                InfoRow(Icons.Filled.Payments, "Internship type", app.type)
                InfoRow(Icons.Filled.CalendarMonth, "Date applied", app.dateText)
                if (app.source.isNotBlank()) InfoRow(Icons.Filled.Link, "Source / Link", app.source)
                if (app.description.isNotBlank()) InfoRow(Icons.Filled.Description, "Job description", app.description)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier.size(36.dp).background(Color(0xFFEEF0FB), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = Blue, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Grey)
            Spacer(Modifier.height(2.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF111827))
        }
    }
}