package com.example.speertechassessment.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.viewmodel.AppViewModel
import com.example.speertechassessment.viewmodel.UiState

@Composable
fun SearchScreen(
    navController: NavHostController,
                 viewModel: AppViewModel
) {
    var username by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()


    //screen content
    Column(modifier = Modifier.padding(AppDimenVal.R.value)) {
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = { Text("Enter username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppDimenVal.R.value))

        Button(
            onClick = { viewModel.getGitHubUser(username) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search",
                color = Color.Black)
        }

        when (val state = uiState) {
            is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            is UiState.Success -> LaunchedEffect(state) {
                navController.navigate(NavigationItem.Profile.route)
            }
            is UiState.NotFound -> Text("User not found", color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }




}