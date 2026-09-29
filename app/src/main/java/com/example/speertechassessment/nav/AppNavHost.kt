package com.example.speertechassessment.nav

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.speertechassessment.data.Repository
import com.example.speertechassessment.ui.screens.FollowersScreen
import com.example.speertechassessment.ui.screens.FollowingScreen
import com.example.speertechassessment.ui.screens.ProfileScreen
import com.example.speertechassessment.ui.screens.SearchScreen
import com.example.speertechassessment.viewmodel.AppViewModel


enum class Screen {
    SEARCH,
    PROFILE,
    FOLLOWERS,
    FOLLOWING
}
sealed class NavigationItem(val route: String) {
    object Search : NavigationItem(Screen.SEARCH.name)
    object Profile : NavigationItem(Screen.PROFILE.name)
    object Followers: NavigationItem(Screen.FOLLOWERS.name)
    object Following: NavigationItem(Screen.FOLLOWING.name)
    // FIX: Not_Found route removed; the profile shows not-found inline, so Back can't loop into it

    // OPT: one place builds "<route>/<username>" (encoded, so any input is a valid route)
    fun withUsername(username: String) = "$route/${Uri.encode(username)}"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavigationItem.Search.route,
    repository: Repository = Repository.default, // injectable for UI tests
    ) {

    // FIX: every destination gets its own ViewModel (scoped to its back-stack entry). One shared
    // instance leaked profile/followers state between users and kept a stale search result alive.
    val vmFactory = remember(repository) { viewModelFactory { initializer { AppViewModel(repository) } } }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination =startDestination
    ) {
        //search
        composable(NavigationItem.Search.route){
            SearchScreen(navController, viewModel(factory = vmFactory))
        }

        //profile
        composable("${NavigationItem.Profile.route}/{username}"){ backstackEntry ->
            val username = backstackEntry.arguments?.getString("username")?:""
            ProfileScreen(username, navController, viewModel(factory = vmFactory))

        }

        //Followers
        composable("${NavigationItem.Followers.route}/{username}") {
                backstackEntry ->
            val username = backstackEntry.arguments?.getString("username")?:""
            FollowersScreen(
                username,
                viewModel(factory = vmFactory),
                navController,
                onBack = {navController.navigateUp()}
            )

        }

        //Following
        composable("${NavigationItem.Following.route}/{username}") {
                backstackEntry ->
            val username = backstackEntry.arguments?.getString("username")?:""
            FollowingScreen (
                username,
                viewModel(factory = vmFactory),
                navController,
                onBack = {navController.navigateUp()}
            )

        }
    }


}
