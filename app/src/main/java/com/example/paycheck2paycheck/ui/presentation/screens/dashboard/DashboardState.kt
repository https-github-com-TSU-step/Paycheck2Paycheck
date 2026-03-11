package com.example.paycheck2paycheck.ui.presentation.screens.dashboard

data class DashboardState(
    val dailyBudget: String = "",
    val remainingAmount: String = "",
    val spentToday: String = "",
    val averageDaily: String = "",
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    // Временно используем список строк для моков. Позже тут будет List<Expense>
    val recentTransactions: List<Pair<String, String>> = emptyList()
)