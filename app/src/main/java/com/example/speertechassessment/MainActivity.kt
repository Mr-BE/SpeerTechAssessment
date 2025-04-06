package com.example.speertechassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.speertechassessment.nav.AppNavHost
import com.example.speertechassessment.ui.screens.SearchScreen
import com.example.speertechassessment.ui.theme.SpeerTechAssessmentTheme
import com.example.speertechassessment.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var keepSplashScreen = true

        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
           keepSplashScreen
        }
        lifecycleScope.launch {
            delay(3000)
            keepSplashScreen = false
        }
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val appViewModel: AppViewModel = viewModel()
            SpeerTechAssessmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        AppNavHost(navController = navController, viewModel = appViewModel )

                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpeerTechAssessmentTheme {
        Greeting("Android")
    }
}