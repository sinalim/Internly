package com.example.internly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val fallbackColors = listOf(
    Color(0xFF3B4FCC), Color(0xFF0D9488), Color(0xFF7C3AED),
    Color(0xFFDC2626), Color(0xFF1E40AF), Color(0xFFF59E0B),
)

private data class LogoStyle(
    val background: Color,
    val icon: ImageVector? = null,
    val iconTint: Color = Color.White,
    val text: String? = null,
)

private fun styleFor(company: String): LogoStyle {
    val key = company.trim().lowercase()
    return when {
        key.contains("techcorp") -> LogoStyle(Color(0xFF0F1B4D), text = "AI")
        key.contains("brightpath") -> LogoStyle(Color(0xFF6D4AFF), icon = Icons.Filled.RocketLaunch)
        key.contains("skylabs") -> LogoStyle(Color(0xFF14B8A6), icon = Icons.Filled.Cloud)
        key.contains("innovatech") -> LogoStyle(Color(0xFFF97316), icon = Icons.Filled.EmojiPeople)
        key.contains("virtusa") -> LogoStyle(Color(0xFF0B1B2B), icon = Icons.Filled.AutoAwesome, iconTint = Color(0xFF22C55E))
        else -> {
            val c = fallbackColors[Math.abs(key.hashCode()) % fallbackColors.size]
            LogoStyle(c, text = company.trim().take(1).uppercase().ifEmpty { "?" })
        }
    }
}

@Composable
fun AppLogo(company: String, size: Dp = 46.dp) {
    val style = styleFor(company)
    Box(
        modifier = Modifier
            .size(size)
            .background(style.background, RoundedCornerShape(size * 0.24f)),
        contentAlignment = Alignment.Center,
    ) {
        if (style.icon != null) {
            Icon(style.icon, contentDescription = company, tint = style.iconTint, modifier = Modifier.size(size * 0.55f))
        } else {
            Text(
                style.text ?: "",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = (size.value * 0.4f).sp,
            )
        }
    }
}
