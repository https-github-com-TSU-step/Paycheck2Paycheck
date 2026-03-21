package com.example.paycheck2paycheck.ui.presentation.screens.budget

data class BudgetSetupUiState(
    val amount: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val days: Int = 0,
    val dailyAmount: String = "",
    val amountError: String? = null,
    val dateError: String? = null
) {
    val isSaveEnabled: Boolean
        get() = amount.isNotEmpty() && 
                startDate.isNotEmpty() && 
                endDate.isNotEmpty() && 
                amountError == null && 
                dateError == null
}
