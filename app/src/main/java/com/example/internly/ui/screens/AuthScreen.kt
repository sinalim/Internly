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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.AuthRepository
import com.example.internly.ui.components.Logo
import com.example.internly.ui.components.PrimaryButton
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.FieldBg
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy

@Composable
fun AuthScreen(onAuthenticated: () -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }

    fun submit() {
        message = null
        if (!isLogin && password != confirm) {
            isError = true
            message = "Passwords do not match"
            return
        }
        loading = true
        val done: (Boolean, String?) -> Unit = { ok, error ->
            loading = false
            if (ok) {
                onAuthenticated()
            } else {
                isError = true
                message = error
            }
        }
        if (isLogin) AuthRepository.logIn(email, password, done)
        else AuthRepository.signUp(email, password, done)
    }

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
            AuthTab("Log in", selected = isLogin) { isLogin = true; message = null }
            AuthTab("Sign Up", selected = !isLogin) { isLogin = false; message = null }
        }
        Spacer(Modifier.height(22.dp))

        AuthTextField("Email Address", email, { email = it }, "you@example.com", keyboard = KeyboardType.Email)
        AuthTextField("Password", password, { password = it }, "••••••••", password = true)
        if (!isLogin) {
            AuthTextField("Re Enter Password", confirm, { confirm = it }, "••••••••", password = true)
        }

        message?.let {
            Text(
                text = it,
                color = if (isError) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            )
        }
        Spacer(Modifier.height(6.dp))

        PrimaryButton(
            label = when {
                loading -> "Please wait..."
                isLogin -> "Log in"
                else -> "Sign Up"
            },
            onClick = { if (!loading) submit() },
        )

        if (isLogin) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Forgot Password?",
                fontSize = 12.sp,
                color = Grey,
                modifier = Modifier.clickable {
                    AuthRepository.resetPassword(email) { ok, error ->
                        isError = !ok
                        message = if (ok) "Password reset email sent. Check your inbox." else error
                    }
                },
            )
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
            onClick = {
                isError = false
                message = "Google sign-in is coming soon"
            },
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
                    modifier = Modifier.clickable { isLogin = false; message = null },
                )
            }
        }
    }
}

@Composable
private fun AuthTextField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    hint: String,
    password: Boolean = false,
    keyboard: KeyboardType = KeyboardType.Text,
) {
    var visible by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(bottom = 4.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(hint, fontSize = 13.sp, color = Grey) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (password) KeyboardType.Password else keyboard
            ),
            visualTransformation = if (password && !visible) PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon = if (password) {
                {
                    IconButton(onClick = { visible = !visible }) {
                        Icon(
                            imageVector = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Show or hide password",
                            tint = Grey,
                        )
                    }
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color(0xFFD1D5DB),
            ),
        )
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
