package com.example.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Graph(
    val route: String
) {
    object Search : Graph("graph_search")
    object Setting : Graph("graph_setting")
}

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int? = null,
    val icon: ImageVector? = null
) {
    object Search : Screen("screen_search", R.string.nav_search, Icons.Filled.Search)
    object Detail : Screen("screen_detail")
    object Setting : Screen("screen_setting", R.string.nav_setting, Icons.Filled.Settings)
}

object Param {
    const val OWNER = "owner"
    const val NAME = "name"
}