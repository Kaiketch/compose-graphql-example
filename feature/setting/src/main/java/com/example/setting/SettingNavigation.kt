package com.example.setting

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.common.Graph
import com.example.common.Screen

fun NavGraphBuilder.settingGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Setting.route, route = Graph.Setting.route) {
        composable(Screen.Setting.route) {
            SettingScreen(settingViewModel = hiltViewModel())
        }
    }
}
