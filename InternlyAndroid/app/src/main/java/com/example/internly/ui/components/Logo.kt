package com.example.internly.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.Blue

@Composable
fun Logo(
    size: Dp = 40.dp,
    color: Color = Blue,
    showText: Boolean = true,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Filled.Eco,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(size),
        )
        if (showText) {
            Text(
                text = "Internly",
                fontSize = (size.value * 0.6f).sp,
                fontWeight = FontWeight.Bold,
                color = color,
            )
        }
    }
}
