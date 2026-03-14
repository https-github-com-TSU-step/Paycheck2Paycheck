package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.BudgetDao
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.data.local.dao.ExpenseDao
import com.example.paycheck2paycheck.data.local.dao.ScheduledPaymentDao
import com.example.paycheck2paycheck.data.local.dao.StreakDao
import com.example.paycheck2paycheck.data.local.entity.StreakEntity
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

        val expenses = expenseDao.getExpensesByBudgetId(id).map { entity: com.example.paycheck2paycheck.data.local.entity.ExpenseEntity ->
            entity.toDomain()
        }

        val payments = paymentDao.getByBudgetId(id).map { entity: com.example.paycheck2paycheck.data.local.entity.ScheduledPaymentEntity ->
            entity.toDomain()
        }
        return budget
    }

    override suspend fun insertBudget(budget: Budget) {
        budgetDao.insert(budget.toEntity())

        val initialStreak = StreakEntity(
            id = java.util.UUID.randomUUID().toString(),
            budgetId = budget.id,
            currentStreak = 0,
            longestStreak = 0,
            lastRecordedDate = null,
            totalDaysTracked = 0
        )
        streakDao.insert(initialStreak)
    }
    override suspend fun getLatestBudget(): Budget? {
        val budgetEntity = budgetDao.getLatestBudget() ?: return null

        val streakEntity = streakDao.getByBudgetId(budgetEntity.id) ?: return null
        val streak = streakEntity.toDomain()

        return budgetEntity.toDomain(streak)
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.update(budget.toEntity())
    }
}