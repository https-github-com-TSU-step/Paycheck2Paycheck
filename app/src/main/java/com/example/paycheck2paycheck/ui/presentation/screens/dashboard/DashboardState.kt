package com.example.paycheck2paycheck.ui.presentation.screens.dashboard

import com.example.paycheck2paycheck.domain.model.Expense

data class DashboardState(
    val dailyBudget: String = "0,00 ₽",
    val remainingAmount: String = "0,00 ₽",
    val spentToday: String = "0,00 ₽",
    val averageDaily: String = "0,00 ₽",
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val recentTransactions: List<Expense> = emptyList()
)