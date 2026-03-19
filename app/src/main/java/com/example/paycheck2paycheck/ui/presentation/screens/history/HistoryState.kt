package com.example.paycheck2paycheck.ui.presentation.screens.history

import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.model.ScheduledPayment

data class HistoryState(
    val totalRemaining: String = "0,00 ₽",
    val daysLeft: Int = 0,
    val scheduledPayments: List<ScheduledPayment> = emptyList(),
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = true,
    val hasLoadedOnce: Boolean = false
)