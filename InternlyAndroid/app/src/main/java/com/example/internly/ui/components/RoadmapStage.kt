package com.example.internly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy

@Composable
fun RoadmapStage(
    number: Int,
    title: String,
    count: String,
    circleColor: Color,
    company: String,
    status: String,
    companyColor: Color,
    isLast: Boolean = false,
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        // Number circle + vertical line
        Column(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = number.toString(),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .width(2.dp)
                        .weight(1f)
                        .background(Color(0xFFC7CEF5)),
                )
            }
        }

        Spacer(Modifier.width(14.dp))

        // Title + application card
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 22.dp),
        ) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
            Text(count, fontSize = 12.sp, color = Grey)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(companyColor, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = company.take(1),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(company, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(status, fontSize = 11.sp, color = Grey)
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Grey)
            }
        }
    }
}
