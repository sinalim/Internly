package com.example.internly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.ChipBg
import com.example.internly.ui.theme.Navy

@Composable
fun StatChip(count: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(ChipBg, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(count, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
        Text(label, fontSize = 11.sp, color = Navy)
    }
}
