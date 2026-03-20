package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun getExpenseById(id: String): Expense?
    fun getExpensesByBudgetId(id: String): Flow<List<Expense>>

    suspend fun updateExpense(expense: Expense)
    suspend fun addExpense(expense: Expense)
}
