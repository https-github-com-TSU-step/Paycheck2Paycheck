package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.ExpenseDao
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override suspend fun getExpenseById(id: String): Expense? {
        return expenseDao.getExpenseById(id)?.toDomain()
    }

    override suspend fun getExpensesByBudgetId(id: String): List<Expense> {
        val entities = expenseDao.getExpensesByBudgetId(id)
        return entities.map { it.toDomain() }
    }

    override suspend fun addExpense(expense: Expense) {
        expenseDao.insert(expense.toEntity())
    }
}