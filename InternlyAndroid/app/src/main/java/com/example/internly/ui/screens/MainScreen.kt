package com.example.internly.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.LightBlue

private data class NavItem(val label: String, val icon: ImageVector)

/** Bottom navigation bar + the five main tabs. */
@Composable
fun MainScreen() {
    val items = listOf(
        NavItem("Home", Icons.Filled.Home),
        NavItem("Roadmap", Icons.Filled.Timeline),
        NavItem("Apply", Icons.Filled.Work),
        NavItem("Prep", Icons.Filled.Psychology),
        NavItem("Profile", Icons.Filled.Person),
    )
    var tab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = tab == index,
                        onClick = { tab = index },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Blue,
                            selectedTextColor = Blue,
                            indicatorColor = LightBlue,
                            unselectedIconColor = Grey,
                            unselectedTextColor = Grey,
                        ),
                    )
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (tab) {
                0 -> HomeScreen()
                1 -> RoadmapScreen()
                2 -> PlaceholderScreen("Apply", Icons.Filled.Work)
                3 -> PlaceholderScreen("Prep", Icons.Filled.Psychology)
                else -> PlaceholderScreen("Profile", Icons.Filled.Person)
            }
        }
    }
}
