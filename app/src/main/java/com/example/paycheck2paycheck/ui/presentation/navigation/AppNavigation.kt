package com.example.paycheck2paycheck.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.paycheck2paycheck.ui.presentation.screens.addexpense.AddExpenseBottomSheet
import com.example.paycheck2paycheck.ui.presentation.screens.budget.BudgetSetupScreen
import com.example.paycheck2paycheck.ui.presentation.screens.dashboard.DashboardScreen
import com.example.paycheck2paycheck.ui.presentation.screens.dashboard.DashboardViewModel
import com.example.paycheck2paycheck.ui.presentation.screens.history.HistoryScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { backStackEntry ->
            val viewModel: DashboardViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            var showAddExpenseSheet by remember { mutableStateOf(false) }

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
                onAddExpenseClick = { showAddExpenseSheet = true },
                onSettingsClick = { navController.navigate("setup") },
                onHistoryClick = { navController.navigate("history") }
            )

            if (showAddExpenseSheet) {
                AddExpenseBottomSheet(
                    onDismiss = {
                        showAddExpenseSheet = false
                        viewModel.loadBudget()
                    }
                )
            }
        }

        composable("setup") {
            BudgetSetupScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { _, _, _ ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("reload", true)
                    navController.popBackStack()
                }
            )
        }

        composable("history") {
            HistoryScreen(
                onMainClick = { navController.popBackStack() }
            )
        }
    }
}