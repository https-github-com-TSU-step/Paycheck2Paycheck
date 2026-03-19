package com.example.paycheck2paycheck.ui.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import com.example.paycheck2paycheck.domain.repository.StreakRepository
import com.example.paycheck2paycheck.domain.usecase.CalculateDailyLimitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val expenseRepository: ExpenseRepository,
    private val streakRepository: StreakRepository,
    private val calculateDailyLimitUseCase: CalculateDailyLimitUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadBudget()
    }

    fun loadBudget() {
        viewModelScope.launch {
            val currentDate = formatCurrentDate()
            val budget = budgetRepository.getLatestBudget()

            if (budget != null) {
                val dailyLimit = calculateDailyLimitUseCase(budget)
                val streak = streakRepository.getStreak(budget.id)

                val expensesList = expenseRepository.getExpensesByBudgetId(budget.id)
                    .firstOrNull() ?: emptyList()

                val expenses = expensesList.sortedByDescending { it.date }

                val today = java.time.LocalDate.now()
                val spentToday = expenses
                    .filter { it.date.toLocalDate() == today }
                    .sumOf { it.amount }

                _state.update {
                    it.copy(
                        currentDate = currentDate,
                        dailyBudget = formatMoney(dailyLimit),
                        remainingAmount = formatMoney(budget.remainingAmount),
                        spentToday = formatMoney(spentToday),
                        averageDaily = formatMoney(dailyLimit),
                        currentStreak = streak?.currentStreak ?: 0,
                        bestStreak = streak?.longestStreak ?: 0,
                        recentTransactions = expenses.take(5)
                    )
                }
            } else {
                _state.update { DashboardState(currentDate = currentDate) }
            }
        }
    }

    private fun formatCurrentDate(): String {
        val now = LocalDateTime.now()
        return now.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))
    }

    private fun formatMoney(amount: Double): String {
        return "%.2f ₽".format(amount).replace(".", ",")
    }
}