package com.example.speertechassessment.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.speertechassessment.ui.screens.ProfileScreen
import com.example.speertechassessment.ui.screens.SearchScreen


enum class Screen {
    SEARCH,
    PROFILE,
}
sealed class NavigationItem(val route: String) {
    object Search : NavigationItem(Screen.SEARCH.name)
    object Profile : NavigationItem(Screen.PROFILE.name)
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Search.route
    ) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination =startDestination
    ) {
        //search
        composable(NavigationItem.Search.route){
            SearchScreen(navController)
        }

        //profile
        composable(NavigationItem.Profile.route){ navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username")?:""
            ProfileScreen(navController, username)

        }
    }


}
