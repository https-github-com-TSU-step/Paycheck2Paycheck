package com.example.paycheck2paycheck.ui.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.usecase.GetTransactionHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    val state: StateFlow<HistoryState> = combine(
        getTransactionHistoryUseCase(),
        budgetRepository.getLatestBudgetFlow()
    ) { history, budget ->
        if (budget == null) {
            HistoryState(isLoading = true)
        } else {
            HistoryState(
                totalRemaining = formatMoney(history.totalRemaining),
                daysLeft = getRemainingDays(budget.endDate),
                scheduledPayments = history.scheduledPayments,
                expenses = history.expenses,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HistoryState(isLoading = true)
    )

    private fun getRemainingDays(endDate: LocalDateTime): Int {
        val now = LocalDateTime.now()
        val end = endDate.toLocalDate()
        val today = now.toLocalDate()
        if (now.isAfter(endDate)) return 0
        return max(0, ChronoUnit.DAYS.between(today, end).toInt())
    }

    private fun formatMoney(amount: Double): String {
        return "%.2f ₽".format(amount).replace(".", ",")
    }
}