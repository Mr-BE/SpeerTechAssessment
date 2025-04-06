package com.example.speertechassessment.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.speertechassessment.ui.screens.FollowersScreen
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

}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Search.route,
    viewModel: AppViewModel
    ) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination =startDestination
    ) {
        //search
        composable(NavigationItem.Search.route){
            SearchScreen(navController, viewModel)
        }

        //profile
        composable(NavigationItem.Profile.route){
            ProfileScreen(navController, viewModel)

        }

        //Followers
        composable(NavigationItem.Followers.route) {
            FollowersScreen(
                viewModel,
                navController,
                onBack = {navController.navigateUp()}
            )

        }
    }


}
