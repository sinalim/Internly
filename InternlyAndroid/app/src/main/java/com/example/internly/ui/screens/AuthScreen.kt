package com.example.internly.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.components.AppField
import com.example.internly.ui.components.Logo
import com.example.internly.ui.components.PrimaryButton
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.FieldBg
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy

@Composable
fun AuthScreen(onAuthenticated: () -> Unit) {
    var isLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))
        Logo(size = 44.dp)
        Spacer(Modifier.height(24.dp))

        // Log in / Sign Up toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(FieldBg, RoundedCornerShape(10.dp))
                .padding(3.dp),
        ) {
            AuthTab("Log in", selected = isLogin) { isLogin = true }
            AuthTab("Sign Up", selected = !isLogin) { isLogin = false }
        }
        Spacer(Modifier.height(22.dp))

        AppField(label = "Email Address", hint = "you@example.com")
        AppField(label = "Password", hint = "••••••••", password = true)
        if (!isLogin) {
            AppField(label = "Re Enter Password", hint = "••••••••", password = true)
        }
        Spacer(Modifier.height(6.dp))

        PrimaryButton(
            label = if (isLogin) "Log in" else "Sign Up",
            onClick = onAuthenticated,
        )

        if (isLogin) {
            Spacer(Modifier.height(12.dp))
            Text("Forgot Password?", fontSize = 12.sp, color = Grey)
        }

        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "or",
                fontSize = 12.sp,
                color = Grey,
                modifier = Modifier.padding(horizontal = 10.dp),
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(18.dp))

        OutlinedButton(
            onClick = onAuthenticated,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color(0xFFD1D5DB)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1F2937)),
        ) {
            Text("G", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFDB4437))
            Spacer(Modifier.width(10.dp))
            Text("Continue with Google")
        }

        if (isLogin) {
            Spacer(Modifier.height(22.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", fontSize = 12.sp, color = Grey)
                Text(
                    text = "Sign up",
                    fontSize = 12.sp,
                    color = Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { isLogin = false },
                )
            }
        }
    }
}

@Composable
private fun RowScope.AuthTab(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Navy else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.White else Color(0xFF1F2937),
        )
    }
}
