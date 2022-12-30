package com.example.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Graph(
    val route: String
) {
    object Home : Graph("graph_home")
    object Setting : Graph("graph_setting")
}

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int? = null,
    val icon: ImageVector? = null
) {
    object Home : Screen("screen_home", R.string.nav_home, Icons.Filled.Home)
    object Setting : Screen("screen_setting", R.string.nav_setting, Icons.Filled.Settings)
}
