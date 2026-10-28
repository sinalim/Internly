package com.example.internly.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.ProfileStore
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy

private val LogoutRed = Color(0xFFDC2626)
private val BorderGrey = Color(0xFFE5E7EB)

@Composable
fun ProfileScreen(onSignOut: () -> Unit) {
    var page by remember { mutableStateOf<String?>(null) }
    var showLogout by remember { mutableStateOf(false) }

    BackHandler(enabled = page != null) { page = null }

    when (page) {
        "account" -> EditProfileScreen(onBack = { page = null }, onSaved = { page = null })
        "about" -> AboutScreen(onBack = { page = null })
        "notifications" -> NotificationsScreen(onBack = { page = null })
        "prefs" -> NotificationPreferencesScreen(onBack = { page = null })
        else -> {
            val name = ProfileStore.name
            val initials = name.split(" ").mapNotNull { it.firstOrNull() }
                .take(2).joinToString("").uppercase().ifEmpty { "?" }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(20.dp),
            ) {
                Text("Profile", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                Spacer(Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Blue, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(initials, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        Text(ProfileStore.email, fontSize = 12.sp, color = Grey)
                    }
                }
                Spacer(Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, BorderGrey, RoundedCornerShape(12.dp)),
                ) {
                    ProfileRow(Icons.Outlined.NotificationsNone, "Notifications") { page = "notifications" }
                    RowDivider()
                    ProfileRow(Icons.Outlined.AccountCircle, "Account Details") { page = "account" }
                    RowDivider()
                    ProfileRow(Icons.Outlined.Tune, "Notification Preferences") { page = "prefs" }
                    RowDivider()
                    ProfileRow(Icons.Outlined.Info, "About") { page = "about" }
                    RowDivider()
                    ProfileRow(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        label = "Logout",
                        color = LogoutRed,
                    ) { showLogout = true }
                }
            }
        }
    }

    if (showLogout) {
        AlertDialog(
            onDismissRequest = { showLogout = false },
            title = { Text("Log out?") },
            text = { Text("You will need to log in again.") },
            confirmButton = {
                TextButton(onClick = {
                    showLogout = false
                    onSignOut()
                }) { Text("Log out", color = LogoutRed) }
            },
            dismissButton = {
                TextButton(onClick = { showLogout = false }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun ProfileRow(
    icon: ImageVector,
    label: String,
    color: Color = Navy,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (color == LogoutRed) LogoutRed else Color(0xFF111827),
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Grey,
        )
    }
}

@Composable
private fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(BorderGrey),
    )
}