package com.example.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.common.Graph
import com.example.common.Screen
import com.example.search.searchGraph
import com.example.setting.settingGraph

@Composable
fun MainScreen(
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isNavigationScreen = bottomNavItems.map { it.route }.contains(currentDestination?.route)

    CompositionLocalProvider(
    ) {
        Scaffold(
            bottomBar = {
                if (isNavigationScreen) {
                    AppBottomNavigation(
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            }
        ) { paddingValues ->
            AppNavHost(navController = navController, paddingValues = paddingValues)
        }
    }
}

@Composable
fun AppBottomNavigation(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomNavigation {
        bottomNavItems.forEach { screen ->
            val icon = screen.icon
            BottomNavigationItem(
                icon = { icon?.let { Icon(icon, null) } },
                label = { screen.resourceId?.let { Text(stringResource(it)) } },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Graph.Search.route,
        modifier = Modifier.padding(paddingValues),
    ) {
        searchGraph(navController = navController)
        settingGraph(navController = navController)
    }
}

val bottomNavItems = listOf(
    Screen.Search,
    Screen.Setting,
)
