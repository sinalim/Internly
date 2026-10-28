package com.example.internly.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.LightBlue

private data class AdminNavItem(val label: String, val icon: ImageVector)

@Composable
fun AdminHome(onSignOut: () -> Unit) {
    val items = listOf(
        AdminNavItem("Dashboard", Icons.Filled.Dashboard),
        AdminNavItem("Users", Icons.Filled.People),
        AdminNavItem("Profile", Icons.Filled.Person),
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
        Box(Modifier.padding(padding)) {
            when (tab) {
                0 -> AdminDashboard()
                1 -> UserManagement()
                2 -> AdminProfileSettings(onLogout = onSignOut)
            }
        }
    }
}