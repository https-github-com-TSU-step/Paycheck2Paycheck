package com.example.paycheck2paycheck.ui.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.StreakRepository // Предполагаем, что у тебя есть этот интерфейс
import com.example.paycheck2paycheck.domain.usecase.CalculateDailyLimitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
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
            // 1. Получаем последний актуальный бюджет
            val budget = budgetRepository.getLatestBudget()

            if (budget != null) {
                // 2. Вычисляем дневной лимит через твой готовый Use Case
                val dailyLimit = calculateDailyLimitUseCase(budget.id)

                // 3. Достаем стрик отдельным запросом
                val streak = streakRepository.getStreak(budget.id)

                // 4. Обновляем UI стейт
                _state.update {
                    it.copy(
                        dailyBudget = formatMoney(dailyLimit),
                        remainingAmount = formatMoney(budget.remainingAmount),
                        spentToday = formatMoney(0.0), // Пока мок для потраченного за сегодня
                        averageDaily = formatMoney(dailyLimit), // Или другой UseCase, если логика отличается
                        currentStreak = streak?.currentStreak ?: 0,
                        bestStreak = streak?.longestStreak ?: 0,
                        recentTransactions = emptyList() // Расходы пока пустые (покажет "НЕТ ДАННЫХ")
                    )
                }
            } else {
                // Если бюджета нет (первый запуск)
                _state.update { DashboardState() }
            }
        }
    }

    private fun formatMoney(amount: Double): String {
        // Форматируем красиво: 5000.0 -> 5000,00 ₽
        return "%.2f ₽".format(amount).replace(".", ",")
    }
}