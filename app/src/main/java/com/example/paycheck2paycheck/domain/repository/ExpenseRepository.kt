package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.Expense

interface ExpenseRepository {
    suspend fun getExpenseById(id: String): Expense?
    suspend fun getExpensesByBudgetId(budgetId: String): List<Expense>
    suspend fun addExpense(expense: Expense)
}
