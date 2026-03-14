package com.example.paycheck2paycheck.ui.presentation.screens.addexpense

data class AddExpenseState(
    val amount: String = "",
    val description: String = "",
    val isScheduled: Boolean = false,
    val scheduledDate: String = "",
    val currentDateTime: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)