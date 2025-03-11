package com.example.repoexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.repoexplorer.screen.HomeScreen
import com.example.repoexplorer.screen.SearchScreen
import com.example.repoexplorer.screen.SignInScreen
import androidx.navigation.compose.rememberNavController
import com.example.repoexplorer.screen.UnavailableScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    NavHost(navController = navController, startDestination = "sign_in") {
        composable("sign_in") {
            SignInScreen(onSignInClick = onSignInClick)
        }
        composable("404") {
            UnavailableScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(
                onSearchClick = { navController.navigate("search") },
                onSignOut = onSignOutClick,
                onFabClick = {navController.navigate("404")}
            )
        }
        composable("search") {
            SearchScreen()
        }
    }
}
