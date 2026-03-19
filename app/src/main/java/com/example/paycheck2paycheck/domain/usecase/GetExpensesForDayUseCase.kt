package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import javax.inject.Inject

class GetExpensesForDayUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend fun getExpensesForDay(budgetId: String, date: LocalDate): List<Expense> {
        val expenses = expenseRepository.getExpensesByBudgetId(budgetId)
            .firstOrNull() ?: emptyList()

        return expenses.filter { it.date.toLocalDate() == date }
    }
}