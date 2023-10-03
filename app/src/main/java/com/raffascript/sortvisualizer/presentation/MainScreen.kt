package com.raffascript.sortvisualizer.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raffascript.sortvisualizer.MyApp
import com.raffascript.sortvisualizer.presentation.navigation.Screen
import com.raffascript.sortvisualizer.presentation.selection.SelectionScreen
import com.raffascript.sortvisualizer.presentation.selection.SelectionViewModel
import com.raffascript.sortvisualizer.presentation.visualizer.VisualizerScreen
import com.raffascript.sortvisualizer.presentation.visualizer.VisualizerViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    MainNavHost(
        navController = navController,
        startDestination = Screen.Selection.route
    )
}

@Composable
fun MainNavHost(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.Selection.route) {
            val viewModel = viewModel<SelectionViewModel>(factory = viewModelFactory {
                SelectionViewModel(MyApp.appModule.algorithmRegister)
            })
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            SelectionScreen(
                state = state,
                navigateToVisualizer = { navController.navigate(Screen.Visualizer.createRoute(it)) }
            )
        }
        composable(route = Screen.Visualizer.route, arguments = Screen.Visualizer.arguments) {
            val viewModel = viewModel<VisualizerViewModel>(factory = viewModelFactory {
                VisualizerViewModel(it, MyApp.appModule.algorithmRegister, MyApp.appModule.userPreferencesRepository)
            })
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            VisualizerScreen(state = state, viewModel::onEvent)
        }
    }
}