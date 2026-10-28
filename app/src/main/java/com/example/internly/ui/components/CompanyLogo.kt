package com.example.internly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Small rounded square with a letter, used as a company logo. */
@Composable
fun CompanyLogo(
    letter: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 42.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.42f).sp,
        )
    }
}
