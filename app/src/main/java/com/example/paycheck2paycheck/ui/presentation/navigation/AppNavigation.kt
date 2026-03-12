package com.example.paycheck2paycheck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.paycheck2paycheck.ui.presentation.screens.budget.BudgetSetupScreen
import com.example.paycheck2paycheck.ui.presentation.screens.dashboard.DashboardScreen
import com.example.paycheck2paycheck.ui.presentation.screens.dashboard.DashboardViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {

        composable("dashboard") { backStackEntry ->
            val viewModel: DashboardViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            // Перезагружаем данные когда возвращаемся с экрана настройки
            val savedStateHandle = backStackEntry.savedStateHandle
            val shouldReload by savedStateHandle.getStateFlow("reload", false).collectAsState()
            LaunchedEffect(shouldReload) {
                if (shouldReload) {
                    viewModel.loadBudget()
                    savedStateHandle["reload"] = false
                }
            }

            DashboardScreen(
                state = state,
                onSettingsClick = { navController.navigate("setup") }
            )
        }

        composable("setup") {
            BudgetSetupScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = { _, _, _ ->
                    // Сигнализируем Dashboard перезагрузить данные
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("reload", true)
                    navController.popBackStack()
                }
            )
        }
    }
}
