package com.example.internly.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.LightBlue
import com.example.internly.ui.theme.Navy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private fun Map<String, Any>.displayName(): String =
    this["fullName"]?.toString()?.takeIf { it.isNotBlank() }
        ?: this["name"]?.toString()?.takeIf { it.isNotBlank() }
        ?: this["displayName"]?.toString()?.takeIf { it.isNotBlank() }
        ?: this["email"]?.toString()?.substringBefore("@")?.takeIf { it.isNotBlank() }
        ?: "Unnamed User"

@Composable
fun UserManagement() {
    var users by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var search by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("All") }
    var selectedUser by remember { mutableStateOf<Map<String, Any>?>(null) }
    var error by remember { mutableStateOf(false) }
    var actionError by remember { mutableStateOf<String?>(null) }
    val myUid = FirebaseAuth.getInstance().currentUser?.uid

    DisposableEffect(Unit) {
        val listener = FirebaseFirestore.getInstance().collection("users")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) error = true
                else {
                    users = snapshot?.documents?.map { it.data.orEmpty() + ("_id" to it.id) } ?: emptyList()
                    error = false
                }
            }
        onDispose { listener.remove() }
    }

    val filtered = users.filter { user ->
        val name = user.displayName()
        val email = user["email"]?.toString().orEmpty()
        val userRole = user["role"]?.toString() ?: "student"
        (name.contains(search, true) || email.contains(search, true)) &&
                (role == "All" || userRole.equals(role, true))
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("User Management", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search by name or email", fontSize = 13.sp, color = Grey) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color(0xFFD1D5DB)
            )
        )
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("All", "Student", "Employer", "Admin").forEach {
                FilterChip(
                    selected = role == it,
                    onClick = { role = it },
                    label = { Text(it) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Navy,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        if (error) {
            Text("Could not load users. Check Firebase permissions.", color = Color(0xFFD32F2F))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it["_id"].toString() }) { user ->
                    val active = user["isActive"] != false
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { selectedUser = user },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(user.displayName(), fontWeight = FontWeight.SemiBold)
                                Text(user["email"]?.toString() ?: "No email", fontSize = 12.sp, color = Grey)
                                Text(
                                    (user["role"]?.toString() ?: "student").replaceFirstChar { it.uppercase() },
                                    fontSize = 12.sp, color = Grey
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (active) LightBlue else Color(0xFFFDECEA)
                            ) {
                                Text(
                                    if (active) "Active" else "Inactive",
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    color = if (active) Blue else Color(0xFFD32F2F)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    selectedUser?.let { user ->
        val active = user["isActive"] != false
        val id = user["_id"].toString()
        val isMe = id == myUid

        AlertDialog(
            onDismissRequest = { selectedUser = null; actionError = null },
            containerColor = Color.White,
            title = { Text(user.displayName()) },
            text = {
                Column {
                    Text(
                        "Email: ${user["email"] ?: "N/A"}\n" +
                                "Role: ${user["role"] ?: "student"}\n" +
                                "Status: ${if (active) "Active" else "Inactive"}"
                    )
                    if (isMe) Text("You can't deactivate your own account.", fontSize = 12.sp, color = Grey)
                    actionError?.let { Text(it, fontSize = 12.sp, color = Color(0xFFD32F2F)) }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isMe,
                    onClick = {
                        FirebaseFirestore.getInstance().collection("users").document(id)
                            .update("isActive", !active)
                            .addOnSuccessListener { selectedUser = null; actionError = null }
                            .addOnFailureListener { actionError = "Could not update this user." }
                    }
                ) { Text(if (active) "Deactivate" else "Activate", color = if (isMe) Grey else Blue) }
            },
            dismissButton = {
                TextButton(onClick = { selectedUser = null; actionError = null }) { Text("Close", color = Grey) }
            }
        )
    }
}