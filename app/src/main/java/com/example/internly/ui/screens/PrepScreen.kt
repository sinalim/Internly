package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.SmartToy
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.ApplicationStore
import com.example.internly.data.JobApplication
import com.example.internly.ui.components.AppLogo
import com.example.internly.ui.theme.AppBg
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy

@Composable
fun PrepScreen() {
    var selectedId by remember { mutableStateOf<Int?>(null) }

    if (selectedId != null) {
        InterviewChatScreen(id = selectedId!!, onBack = { selectedId = null })
        return
    }

    val interviews = ApplicationStore.applications.filter { it.status == "Interview" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text("Prep Hub", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
        Spacer(Modifier.height(20.dp))
        Text("Interview Prep", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
        Text("Your upcoming interviews, powered by AI", fontSize = 13.sp, color = Grey)
        Spacer(Modifier.height(16.dp))

        if (interviews.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.size(110.dp).background(Color(0xFFE8ECFB), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.SmartToy, contentDescription = null, tint = Blue, modifier = Modifier.size(56.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("No interviews yet", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Prep will appear here once you're scheduled for an interview.",
                    fontSize = 13.sp,
                    color = Grey,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            interviews.forEach { app ->
                InterviewCard(app = app, onClick = { selectedId = app.id })
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun InterviewCard(app: JobApplication, onClick: () -> Unit) {
    val ready = app.doneTodos.isNotEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        AppLogo(app.company, size = 46.dp)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(app.company, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            Text(app.role, fontSize = 13.sp, color = Grey)
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .background(Color(0xFFEEF0FB), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(if (ready) Color(0xFF22C55E) else Color(0xFFEF4444), CircleShape),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (ready) "Prep ready" else "Not started",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Navy,
                )
            }
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Grey)
    }
}