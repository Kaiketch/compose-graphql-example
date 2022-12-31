package com.example.search

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.common.Graph
import com.example.common.Screen

fun NavGraphBuilder.searchGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Search.route, route = Graph.Search.route) {
        composable(Screen.Search.route) {
            SearchScreen(
                searchViewModel = hiltViewModel(),
            )
        }
    }
}
