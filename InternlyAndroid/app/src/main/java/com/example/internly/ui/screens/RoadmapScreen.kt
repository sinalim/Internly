package com.example.internly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.components.RoadmapStage
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Green
import com.example.internly.ui.theme.Navy
import com.example.internly.ui.theme.Orange
import com.example.internly.ui.theme.SkyBlue
import com.example.internly.ui.theme.Teal

@Composable
fun RoadmapScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 16.dp),
    ) {
        Text(
            text = "Your Roadmap",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Navy,
        )
        Spacer(Modifier.height(24.dp))

        RoadmapStage(
            number = 1,
            title = "Applied",
            count = "3 applications",
            circleColor = Navy,
            company = "TechCorp",
            status = "Applied · 3 days ago",
            companyColor = Teal,
        )
        RoadmapStage(
            number = 2,
            title = "Assessment",
            count = "2 applications",
            circleColor = Blue,
            company = "BrightPath",
            status = "Follow-Up · 3 days ago",
            companyColor = Orange,
        )
        RoadmapStage(
            number = 3,
            title = "Interview",
            count = "3 applications",
            circleColor = Blue,
            company = "SkyLabs",
            status = "Assessment · 5 days ago",
            companyColor = SkyBlue,
        )
        RoadmapStage(
            number = 4,
            title = "Offer",
            count = "4 applications",
            circleColor = Green,
            company = "InnovaTech",
            status = "Offer · 5 days ago",
            companyColor = Green,
            isLast = true,
        )
    }
}
