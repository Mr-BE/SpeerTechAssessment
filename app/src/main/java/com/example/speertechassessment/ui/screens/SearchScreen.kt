package com.example.speertechassessment.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    var username by rememberSaveable { mutableStateOf("") } // FIX: `remember` lost the query on rotation/Back
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val search = { viewModel.getGitHubUser(username) }


    //screen content
    Column(modifier = Modifier.padding(AppDimenVal.R.value)) {
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = { Text("Enter username") },
            singleLine = true,
            // OPT: the keyboard's search key triggers the search too
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { search() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppDimenVal.R.value))

        Button(
            onClick = search,
            // OPT: no duplicate requests while one is running, and nothing to search for blank input
            enabled = uiState !is UiState.Loading && username.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search",
                style = MaterialTheme.typography.bodyMedium)
        }

        when (val state = uiState) {
            is UiState.Loading -> SkeletonProfileCard() //shimmer load
            is UiState.Success -> LaunchedEffect(state) {
                // FIX: route uses the login from the API (was the raw text field value, "" after Back)
                navController.navigate(NavigationItem.Profile.withUsername(state.user.login)){
                    launchSingleTop = true // FIX: was the no-op expression `launchSingleTop`
                }
                viewModel.clearSearchResult() // FIX: consume the result so Back doesn't re-navigate
            }
            is UiState.NotFound -> Text("User not found 😔", color = MaterialTheme.colorScheme.error)
            is UiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error) // FIX: real reason
            UiState.Idle -> {}
        }
    }




}
