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
fun FollowersScreen(
    username:String,
    viewModel: AppViewModel = viewModel(),
    navController: NavController,
    onBack: () -> Unit
){
    val followers by viewModel.followers.collectAsStateWithLifecycle()

    // FIX: loading state now lives in the ViewModel. The local isLoading flag flipped to false before
    // the request started, so the spinner/shimmer never reflected it and another user's list could flash.
    LaunchedEffect(username) {
        viewModel.getFollowers(username)
    }

    UserListScreen(
        title = "Followers of $username",
        username = username,
        state = followers,
        emptyMessage = "$username has no followers yet",
        onRefresh = { viewModel.getFollowers(username, refresh = true) },
        onUserClick = { user -> navController.navigate(NavigationItem.Profile.withUsername(user.login)) },
        onBack = onBack,
    )
}
