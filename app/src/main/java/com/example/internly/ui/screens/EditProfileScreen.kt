package com.example.internly.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.ProfileStore
import com.example.internly.ui.components.FormLabel
import com.example.internly.ui.components.FormTextField
import com.example.internly.ui.components.PrimaryButton
import com.example.internly.ui.components.ScreenHeader
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.FieldBg
import com.example.internly.ui.theme.Grey

@Composable
fun EditProfileScreen(onBack: () -> Unit, onSaved: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(ProfileStore.name) }
    var error by remember { mutableStateOf<String?>(null) }

    val initials = name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ScreenHeader(title = "Update Profile", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Blue, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials.ifEmpty { "?" },
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(24.dp))

            FormTextField("Full name", name, { name = it; error = null }, "Your name", required = true)

            // The login email is shown but can't be changed here
            Column(modifier = Modifier.fillMaxWidth()) {
                FormLabel("Email")
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FieldBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                ) {
                    Text(ProfileStore.email, fontSize = 14.sp, color = Grey)
                }
                Spacer(Modifier.height(14.dp))
            }

            error?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color(0xFFDC2626),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                )
            }

            PrimaryButton(
                label = "Save changes",
                onClick = {
                    if (name.isBlank()) {
                        error = "Please enter your name."
                    } else {
                        ProfileStore.name = name.trim()
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                        onSaved()
                    }
                },
            )
        }
    }
}