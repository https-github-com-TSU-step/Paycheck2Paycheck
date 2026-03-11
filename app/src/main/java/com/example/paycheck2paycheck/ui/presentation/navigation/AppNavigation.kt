package com.example.paycheck2paycheck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.paycheck2paycheck.ui.presentation.screens.budget.BudgetSetupScreen
import com.example.paycheck2paycheck.ui.presentation.screens.dashboard.DashboardScreen

@Composable
fun AppNavigation() {
    // NavController - это "руль" нашего приложения
    val navController = rememberNavController()

    // NavHost - это карта экранов. startDestination указывает, что показывать первым
    NavHost(navController = navController, startDestination = "dashboard") {

        // 1. Главный экран
        composable("dashboard") {
            DashboardScreen(
                // Передаем колбэк для клика по шестеренке
                onSettingsClick = {
                    navController.navigate("setup")
                }
                // (Не забудь добавить onSettingsClick внутрь твоего DashboardScreen и прокинуть его в MainTopBar)
            )
        }

        // 2. Экран настройки бюджета
        composable("setup") {
            BudgetSetupScreen(
                // Возврат по стрелочке
                onBackClick = {
                    navController.popBackStack()
                },
                // Сохранение бюджета
                onSaveClick = { amount, startDate, endDate ->
                    // Вызываем логику сохранения во ViewModel (которую ты уже написал),
                    // а затем возвращаемся назад
                    navController.popBackStack()
                }
            )
        }
    }
}