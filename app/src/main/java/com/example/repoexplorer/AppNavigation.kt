package com.example.repoexplorer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.repoexplorer.screen.HomeScreen
import com.example.repoexplorer.screen.SearchScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onSearchClick = {
                navController.navigate("search")
            })
        }
        composable("search") {
            SearchScreen()
        }
    }
}
