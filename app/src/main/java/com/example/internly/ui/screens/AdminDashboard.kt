package com.example.internly.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.LightBlue
import com.example.internly.ui.theme.Navy
import com.google.firebase.firestore.FirebaseFirestore

private fun Map<String, Any>.roleOf() = this["role"]?.toString()?.lowercase() ?: "student"

private fun Map<String, Any>.displayName(): String =
    this["fullName"]?.toString()?.takeIf { it.isNotBlank() }
        ?: this["name"]?.toString()?.takeIf { it.isNotBlank() }
        ?: this["displayName"]?.toString()?.takeIf { it.isNotBlank() }
        ?: this["email"]?.toString()?.substringBefore("@")?.takeIf { it.isNotBlank() }
        ?: "Unnamed User"

@Composable
fun AdminDashboard() {
    var users by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener = FirebaseFirestore.getInstance().collection("users")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) error = true
                else {
                    users = snapshot?.documents?.map { it.data.orEmpty() + ("_id" to it.id) } ?: emptyList()
                    error = false
                }
                loading = false
            }
        onDispose { listener.remove() }
    }

    val recent = users.sortedByDescending { (it["createdAt"] as? Number)?.toLong() ?: 0L }.take(5)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Admin Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy)
            Text("Welcome to Internly administration", fontSize = 13.sp, color = Grey)
        }

        when {
            loading -> item { CircularProgressIndicator() }
            error -> item { Text("Failed to load dashboard data.", color = Color(0xFFD32F2F)) }
            else -> {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard("Total Users", users.size, Modifier.weight(1f))
                        StatCard("Students", users.count { it.roleOf() == "student" }, Modifier.weight(1f))
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard("Employers", users.count { it.roleOf() == "employer" }, Modifier.weight(1f))
                        StatCard("Admins", users.count { it.roleOf() == "admin" }, Modifier.weight(1f))
                    }
                }
                item {
                    Text("Recently Registered", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Navy)
                }
                items(recent, key = { it["_id"].toString() }) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(user.displayName(), fontWeight = FontWeight.SemiBold)
                            Text(user["email"]?.toString() ?: "No email", fontSize = 12.sp, color = Grey)
                            Text(user.roleOf().replaceFirstChar { it.uppercase() }, fontSize = 12.sp, color = Grey)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, count: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightBlue)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontSize = 12.sp, color = Grey)
            Text("$count", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Navy)
        }
    }
}