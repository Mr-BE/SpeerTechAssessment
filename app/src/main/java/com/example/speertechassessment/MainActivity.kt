package com.example.speertechassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.speertechassessment.nav.AppNavHost
import com.example.speertechassessment.ui.theme.SpeerTechAssessmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // OPT: splash now closes when the first frame is ready. The 3 s keep-on-screen hold delayed every
        // launch and re-ran on each rotation, blocking drawing for 3 s after every config change.
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeerTechAssessmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        navController = rememberNavController(),
                        // FIX: consume the insets we pad for; each TopAppBar added the status bar again
                        modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding),
                    )
                }
            }
        }
    }
}

// OPT: removed the unused template Greeting/GreetingPreview composables
