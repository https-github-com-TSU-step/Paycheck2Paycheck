package com.example.paycheck2paycheck.ui.presentation.screens.history

import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.usecase.GetTransactionHistoryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

@Singleton
class HistoryDataHolder @Inject constructor(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val budgetRepository: BudgetRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val state: StateFlow<HistoryState> = combine(
        getTransactionHistoryUseCase(),
        budgetRepository.getLatestBudgetFlow()
    ) { history, budget ->
        HistoryState(
            totalRemaining = formatMoney(history.totalRemaining),
            daysLeft = if (budget != null) getRemainingDays(budget.endDate) else 0,
            scheduledPayments = history.scheduledPayments,
            expenses = history.expenses,
            isLoading = false,
            hasLoadedOnce = true
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = HistoryState(
            isLoading = true,
            hasLoadedOnce = false,
            totalRemaining = "—",
            daysLeft = -1
        )
    )

    private fun getRemainingDays(endDate: LocalDateTime): Int {
        val now = LocalDateTime.now()
        if (now.isAfter(endDate)) return 0
        return max(1, ChronoUnit.DAYS.between(now.toLocalDate(), endDate.toLocalDate()).toInt())
    }

    private fun formatMoney(amount: Double): String {
        return "%.2f ₽".format(amount).replace(".", ",")
    }
}