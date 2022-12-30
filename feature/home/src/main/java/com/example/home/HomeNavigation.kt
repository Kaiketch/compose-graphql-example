package com.example.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.common.Graph
import com.example.common.Screen

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Home.route, route = Graph.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                homeViewModel = hiltViewModel(),
            )
        }
    }
}
