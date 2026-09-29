package com.example.speertechassessment.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.viewmodel.AppViewModel


@Composable
fun FollowingScreen(
    username:String,
    viewModel: AppViewModel = viewModel(),
    navController: NavController,
    onBack: () -> Unit
){
    val following by viewModel.following.collectAsStateWithLifecycle()

    LaunchedEffect(username) {
        viewModel.getFollowing(username)
    }

    // FIX: now has loading, pull-to-refresh, empty and error states (it had none)
    UserListScreen(
        title = "$username's Following",
        username = username,
        state = following,
        emptyMessage = "$username isn't following anyone yet",
        onRefresh = { viewModel.getFollowing(username, refresh = true) },
        onUserClick = { user -> navController.navigate(NavigationItem.Profile.withUsername(user.login)) },
        onBack = onBack,
    )
}
