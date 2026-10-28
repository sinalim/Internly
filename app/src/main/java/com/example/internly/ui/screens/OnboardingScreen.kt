package com.example.internly.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.R
import com.example.internly.ui.components.PrimaryButton
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import kotlinx.coroutines.launch

private data class OnbPage(
    @DrawableRes val image: Int,
    val title: String,
    val subtitle: String,
)

@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    val pages = remember {
        listOf(
            OnbPage(
                R.drawable.img_1,
                "Track every application in one place",
                "Keep all your internships, deadlines and updates organized.",
            ),
            OnbPage(
                R.drawable.img_2,
                "Never miss a follow-up or deadline",
                "Get timely reminders and stay on top of your next steps.",
            ),
            OnbPage(
                R.drawable.img,
                "Prep for interviews with AI",
                "Get personalized feedback, questions and confidence for each role.",
            ),
        )
    }
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == pages.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { index ->
            val page = pages[index]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = page.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit,
                )
                Spacer(Modifier.height(28.dp))
                Text(
                    text = page.title,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Blue,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = page.subtitle,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Grey,
                )
            }
        }

        // Page dots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pages.size) { i ->
                val selected = i == pagerState.currentPage
                val dotSize by animateDpAsState(if (selected) 10.dp else 7.dp, label = "dot")
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(if (selected) Blue else Color(0xFFB8C0F0)),
                )
            }
        }

        Box(modifier = Modifier.padding(start = 24.dp, top = 20.dp, end = 24.dp, bottom = 24.dp)) {
            PrimaryButton(
                label = if (isLast) "Get Started" else "Next",
                trailingIcon = if (isLast) null else Icons.AutoMirrored.Filled.ArrowForward,
                onClick = {
                    if (isLast) onDone()
                    else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                },
            )
        }
    }
}