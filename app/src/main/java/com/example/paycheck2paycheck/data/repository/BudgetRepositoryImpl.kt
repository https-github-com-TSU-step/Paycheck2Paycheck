package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.BudgetDao
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.data.local.dao.ExpenseDao
import com.example.paycheck2paycheck.data.local.dao.ScheduledPaymentDao
import com.example.paycheck2paycheck.data.local.dao.StreakDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val expenseDao: ExpenseDao,
    private val paymentDao: ScheduledPaymentDao,
    private val streakDao: StreakDao
) : BudgetRepository {

    override suspend fun getBudgetById(id: String): Budget? {

        val budgetEntity = budgetDao.getBudgetById(id) ?: return null

        val streakEntity = streakDao.getByBudgetId(id) ?: return null
        val streak = streakEntity.toDomain()

        val budget = budgetEntity.toDomain(streak)

        val expenses =
            expenseDao.getExpensesForBudget(id).first().map { it.toDomain() }

        val payments =
            paymentDao.getByBudgetId(id).map { it.toDomain() }

        budget.expenses.addAll(expenses)
        budget.scheduledPayments.addAll(payments)

        return budget
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.update(budget.toEntity())
    }
}