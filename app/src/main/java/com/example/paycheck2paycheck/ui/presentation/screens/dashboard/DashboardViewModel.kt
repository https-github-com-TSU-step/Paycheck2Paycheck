package com.example.paycheck2paycheck.ui.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import com.example.paycheck2paycheck.domain.repository.StreakRepository
import com.example.paycheck2paycheck.domain.usecase.CalculateDailyLimitUseCase
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
            val budget = budgetRepository.getLatestBudget()

            if (budget != null) {
                val dailyLimit = calculateDailyLimitUseCase(budget.id)
                val streak = streakRepository.getStreak(budget.id)

                // 1. Загружаем список трат
                val expenses = expenseRepository.getExpensesByBudgetId(budget.id)
                    .sortedByDescending { it.date } // Самые свежие сверху

                // 2. Считаем, сколько потрачено конкретно сегодня
                val today = java.time.LocalDate.now()
                val spentToday = expenses
                    .filter { it.date.toLocalDate() == today }
                    .sumOf { it.amount }

                _state.update {
                    it.copy(
                        dailyBudget = formatMoney(dailyLimit),
                        remainingAmount = formatMoney(budget.remainingAmount),
                        spentToday = formatMoney(spentToday),
                        averageDaily = formatMoney(dailyLimit),
                        currentStreak = streak?.currentStreak ?: 0,
                        bestStreak = streak?.longestStreak ?: 0,
                        recentTransactions = expenses // Передаем список в стейт
                    )
                }
            }
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

    private fun formatMoney(amount: Double): String {
        return "%.2f ₽".format(amount).replace(".", ",")
    }
}