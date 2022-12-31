package com.example.search

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.common.Graph
import com.example.common.Param
import com.example.common.Screen

fun NavGraphBuilder.searchGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Search.route, route = Graph.Search.route) {
        composable(Screen.Search.route) {
            SearchScreen(
                searchViewModel = hiltViewModel(),
                navigateToDetail = { owner, name ->
                    navController.navigate("${Screen.Detail.route}/$owner/$name")
                }
            )
        }
        composable(
            "${Screen.Detail.route}/{${Param.OWNER}}/{${Param.NAME}}",
            arguments = listOf(
                navArgument(Param.OWNER) { type = NavType.StringType },
                navArgument(Param.NAME) { type = NavType.StringType },
            ),
        ) {
            DetailScreen(
                detailViewModel = hiltViewModel(),
            )
        }
    }
}
