package com.example.speertechassessment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.sizedAvatarUrl
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.viewmodel.AppViewModel
import com.example.speertechassessment.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(username:String, navController:NavHostController, vm: AppViewModel = viewModel()){

    val state by vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect  (username){
        vm.getGitHubUser(username) // no-op when this entry already shows the user (e.g. after Back)
    }

    Column {
        // FIX: top bar always shown (title comes from the route), so Back works while loading or on error
        TopAppBar(title = { Text(username) }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })

        when(val s = state){
            UiState.Idle, UiState.Loading -> SkeletonProfileCard(Modifier.padding(AppDimenVal.R.value))
            // FIX: rendered inline. navigate() was called during composition, pushing a NOT_FOUND entry on
            // every recomposition and trapping Back in a loop.
            UiState.NotFound -> UserNotFoundScreen(navController)
            is UiState.Error -> UserNotFoundScreen(navController, message = s.message, onRetry = { vm.getGitHubUser(username) })
            is UiState.Success -> ProfileContent(user = s.user, navController =  navController)
        }
    }
}

// FIX: text colours now come from the theme (onSurface); hard-coded Color.Black was invisible in dark mode.
// OPT: dropped the needless `user?.` / `if (user != null)` checks on a non-null user.
@Composable fun ProfileContent(user: GitHubUser, navController: NavHostController){
    val avatarPx = with(LocalDensity.current) { AppDimenVal.XXL.value.roundToPx() }

    Column(modifier = Modifier.padding(AppDimenVal.R.value)) {

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth() // FIX: content is now centred in the card whatever its width
                    .padding(horizontal = AppDimenVal.XL.value, vertical = AppDimenVal.R.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    // OPT: fixed size (no layout jump when the image arrives) and a right-sized download
                    modifier = Modifier
                        .size(AppDimenVal.XXL.value)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    model = user.sizedAvatarUrl(avatarPx),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(AppDimenVal.R.value))
                user.name?.let {
                    Text(it, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                }
                Text("@${user.login}", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(AppDimenVal.R.value))

                Row(horizontalArrangement = Arrangement.Center) { //Followers and following row

                    Column(
                        modifier = Modifier.clickable {
                            navController.navigate(NavigationItem.Followers.withUsername(user.login))
                        },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {//Followers
                        Text(
                            "${user.followers}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            "Followers",
                            style = MaterialTheme.typography.bodyMedium)

                    }

                    Spacer(modifier = Modifier.width(AppDimenVal.XL.value))

                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            navController.navigate(NavigationItem.Following.withUsername(user.login))
                        },) {//Following
                        Text(
                            "${user.following}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

                        )
                        Text(
                            "Following",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(AppDimenVal.L.value))

                Row {
                    Text(
                        user.bio ?: "No Bio Available",
                        style = MaterialTheme.typography.bodyMedium)
                }

            }
        }

        Spacer(modifier = Modifier.height(AppDimenVal.R.value))
    }
}
