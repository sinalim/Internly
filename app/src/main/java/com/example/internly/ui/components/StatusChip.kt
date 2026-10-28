package com.example.internly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.Navy

/** Pill with a coloured dot and a label, e.g. "Applied", "Prep ready". */
@Composable
fun StatusChip(label: String, dotColor: Color) {
    Row(
        modifier = Modifier
            .background(Color(0xFFEEF1FB), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(dotColor, CircleShape),
        )
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Navy)
    }
}
