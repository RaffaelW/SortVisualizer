package com.raffascript.sortvisualizer.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

private const val SELECTION_ROUTE = "selection"

private const val VISUALIZER_ROUTE = "visualizer"
private const val VISUALIZER_ALGORITHM_ID = "algorithmId"

sealed class Screen(val route: String) {

    object Selection : Screen(SELECTION_ROUTE) {
        fun createRoute() = route
    }

    object Visualizer : Screen("$VISUALIZER_ROUTE/{$VISUALIZER_ALGORITHM_ID}") {
        val arguments = listOf(navArgument(VISUALIZER_ALGORITHM_ID) { type = NavType.IntType })
        const val argAlgorithmId = VISUALIZER_ALGORITHM_ID

        fun createRoute(algorithmId: Int) = "$VISUALIZER_ROUTE/$algorithmId"
    }
}