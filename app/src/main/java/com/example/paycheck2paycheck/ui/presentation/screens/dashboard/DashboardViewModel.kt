package com.example.paycheck2paycheck.ui.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paycheck2paycheck.domain.repository.StreakRepository
import com.example.paycheck2paycheck.domain.usecase.GetTransactionHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase, // Используем наш живой поток
    private val streakRepository: StreakRepository
) : ViewModel() {

    // Превращаем Flow в State для Compose
    val state: StateFlow<DashboardState> = getTransactionHistoryUseCase()
        .map { history ->
            val today = java.time.LocalDate.now()
            val spentToday = history.expenses
                .filter { it.date.toLocalDate() == today }
                .sumOf { it.amount }

            // Здесь можно вызывать streakRepository.getStreak(history.budgetId)
            // Но лучше, если стрик тоже будет частью TransactionHistory

            DashboardState(
                currentDate = formatCurrentDate(),
                dailyBudget = formatMoney(history.totalRemaining), // или твой лимит
                remainingAmount = formatMoney(history.totalRemaining),
                spentToday = formatMoney(spentToday),
                recentTransactions = history.expenses,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardState(isLoading = true) // Экран сразу знает, что нужно подождать
        )

    private fun formatCurrentDate(): String {
        val now = LocalDateTime.now()
        return now.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))
    }

    private fun formatMoney(amount: Double): String = "%.2f ₽".format(amount).replace(".", ",")
}