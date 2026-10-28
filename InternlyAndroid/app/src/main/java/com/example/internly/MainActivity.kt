package com.example.internly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.internly.ui.screens.AuthScreen
import com.example.internly.ui.screens.MainScreen
import com.example.internly.ui.screens.OnboardingScreen
import com.example.internly.ui.screens.SplashScreen
import com.example.internly.ui.theme.InternlyTheme

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

private enum class Step { Splash, Onboarding, Auth, Main }

@Composable
private fun InternlyApp() {
    var step by rememberSaveable { mutableStateOf(Step.Splash) }

    when (step) {
        Step.Splash -> SplashScreen(onFinished = { step = Step.Onboarding })
        Step.Onboarding -> OnboardingScreen(onDone = { step = Step.Auth })
        Step.Auth -> AuthScreen(onAuthenticated = { step = Step.Main })
        Step.Main -> MainScreen()
    }
}
