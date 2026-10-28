
package com.example.internly.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.components.PrimaryButton
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.LightBlue
import com.example.internly.ui.theme.Navy
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminProfileSettings(onLogout: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var name by remember { mutableStateOf("Loading profile...") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(true) }
    var updating by remember { mutableStateOf(false) }

    LaunchedEffect(user?.uid) {
        if (user != null) {
            FirebaseFirestore.getInstance().collection("users").document(user.uid).get()
                .addOnSuccessListener { name = it.getString("fullName") ?: "Admin" }
                .addOnFailureListener { name = "Admin" }
        } else name = "Not signed in"
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Profile & Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlue)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text(user?.email ?: "No email available", fontSize = 13.sp, color = Grey)
                Text("Administrator", fontSize = 12.sp, color = Blue, fontWeight = FontWeight.SemiBold)
            }
        }

        Text("Change Password", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Navy)

        PasswordField("Current password", currentPassword) { currentPassword = it }
        PasswordField("New password", newPassword) { newPassword = it }

        if (message.isNotBlank()) {
            Text(
                message,
                fontSize = 12.sp,
                color = if (isError) Color(0xFFD32F2F) else Color(0xFF2E7D32)
            )
        }

        PrimaryButton(
            label = if (updating) "Updating..." else "Update Password",
            onClick = {
                if (updating) return@PrimaryButton
                val currentUser = auth.currentUser
                val email = currentUser?.email
                isError = true
                if (currentUser == null || email == null) {
                    message = "Please sign in again."
                } else if (currentPassword.isBlank() || newPassword.length < 6) {
                    message = "Enter your current password and a new password of at least 6 characters."
                } else {
                    updating = true
                    val credential = EmailAuthProvider.getCredential(email, currentPassword)
                    currentUser.reauthenticate(credential)
                        .addOnSuccessListener {
                            currentUser.updatePassword(newPassword)
                                .addOnSuccessListener {
                                    currentPassword = ""; newPassword = ""
                                    isError = false
                                    message = "Password updated successfully."
                                    updating = false
                                }
                                .addOnFailureListener {
                                    message = "Could not update password."
                                    updating = false
                                }
                        }
                        .addOnFailureListener {
                            message = "Current password is incorrect."
                            updating = false
                        }
                }
            }
        )

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth().height(46.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = onLogout
        ) { Text("Log Out", color = Navy) }
    }
}

@Composable
private fun PasswordField(label: String, value: String, onChange: (String) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "Show or hide password",
                    tint = Grey
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue,
            unfocusedBorderColor = Color(0xFFD1D5DB)
        )
    )
}