package com.example.internly.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.components.CompanyLogo
import com.example.internly.ui.components.ScreenHeader
import com.example.internly.ui.theme.Grey

private data class NotificationItem(
    val title: String,
    val company: String,
    val time: String,
    val letter: String,
    val color: Color,
)

private val notificationSections = listOf(
    "Today" to listOf(
        NotificationItem("Email Reminder", "SkyLabs", "2h ago", "S", Color(0xFF14B8A6)),
        NotificationItem("Prep work", "Virtusa", "4h ago", "V", Color(0xFF0F172A)),
    ),
    "This Week" to listOf(
        NotificationItem("Email Reminder", "InnovaTech", "4d ago", "I", Color(0xFFF97316)),
        NotificationItem("Application due", "Virtusa", "6d ago", "V", Color(0xFF0F172A)),
    ),
    "Earlier" to listOf(
        NotificationItem("Email Reminder", "InnovaTech", "10d ago", "I", Color(0xFFF97316)),
        NotificationItem("Research due", "Virtusa", "16d ago", "V", Color(0xFF0F172A)),
    ),
)

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ScreenHeader(title = "Notifications", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            notificationSections.forEach { (section, items) ->
                Text(
                    text = section,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                )
                items.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CompanyLogo(letter = item.letter, color = item.color, size = 38.dp)
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(item.company, fontSize = 12.sp, color = Grey)
                            }
                            Text(item.time, fontSize = 11.sp, color = Grey)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
