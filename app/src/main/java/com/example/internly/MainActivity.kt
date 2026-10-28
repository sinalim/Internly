package com.example.internly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.internly.data.AuthRepository
import com.example.internly.ui.screens.AdminHome
import com.example.internly.ui.screens.AuthScreen
import com.example.internly.ui.screens.MainScreen
import com.example.internly.ui.screens.OnboardingScreen
import com.example.internly.ui.screens.SplashScreen
import com.example.internly.ui.theme.InternlyTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternlyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    InternlyApp()
                }
            }
        }
    }
}

private enum class Step { Splash, Onboarding, Auth, Main, Admin }

@Composable
private fun InternlyApp() {
    var step by remember { mutableStateOf(Step.Splash) }

    // Back on the login screen goes to onboarding.
    // On the main screen, Back closes the app and the user stays logged in.
    BackHandler(enabled = step == Step.Auth) {
        step = Step.Onboarding
    }

    // After login (or when the app opens while logged in), send the user to the
    // right place: admins to the admin screens, deactivated accounts back to login,
    // everyone else to the normal main screen.
    fun routeByRole() {
        AuthRepository.checkAccount { active, role ->
            step = when {
                !active -> {
                    AuthRepository.logOut()
                    Step.Auth
                }
                role == "admin" -> Step.Admin
                else -> Step.Main
            }
        }
    }

    when (step) {
        Step.Splash -> {
            LaunchedEffect(Unit) {
                delay(4000) // splash time in milliseconds
                if (AuthRepository.isLoggedIn) routeByRole() else step = Step.Onboarding
            }
            SplashScreen()
        }
        Step.Onboarding -> OnboardingScreen(onDone = { step = Step.Auth })
        Step.Auth -> AuthScreen(onAuthenticated = { routeByRole() })
        Step.Main -> MainScreen(onSignOut = {
            AuthRepository.logOut()
            step = Step.Auth
        })
        Step.Admin -> AdminHome(onSignOut = {
            AuthRepository.logOut()
            step = Step.Auth
        })
    }
}