package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.internly.ui.components.ScreenHeader
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey

@Composable
fun NotificationPreferencesScreen(onBack: () -> Unit) {
    var emailReminders by remember { mutableStateOf(true) }
    var deadlineAlerts by remember { mutableStateOf(true) }
    var prepTips by remember { mutableStateOf(false) }
    var applicationUpdates by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ScreenHeader(title = "Notification Preferences", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            PreferenceRow("Email reminders", "Follow-up reminders by email", emailReminders) { emailReminders = it }
            HorizontalDivider(color = Color(0xFFE5E7EB))
            PreferenceRow("Deadline alerts", "Get alerted before deadlines", deadlineAlerts) { deadlineAlerts = it }
            HorizontalDivider(color = Color(0xFFE5E7EB))
            PreferenceRow("Interview prep tips", "AI tips before your interviews", prepTips) { prepTips = it }
            HorizontalDivider(color = Color(0xFFE5E7EB))
            PreferenceRow("Application updates", "Status changes on your applications", applicationUpdates) { applicationUpdates = it }
        }
    }
}

@Composable
private fun PreferenceRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, fontSize = 12.sp, color = Grey)
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Blue,
            ),
        )
    }
}
