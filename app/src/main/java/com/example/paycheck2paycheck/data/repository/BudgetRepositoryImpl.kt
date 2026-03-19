package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.BudgetDao
import com.example.paycheck2paycheck.data.local.dao.ExpenseDao
import com.example.paycheck2paycheck.data.local.dao.ScheduledPaymentDao
import com.example.paycheck2paycheck.data.local.dao.StreakDao
import com.example.paycheck2paycheck.data.local.entity.StreakEntity
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val expenseDao: ExpenseDao,
    private val paymentDao: ScheduledPaymentDao,
    private val streakDao: StreakDao
) : BudgetRepository {

    override suspend fun getBudgetById(id: String): Budget? {
        val budgetEntity = budgetDao.getBudgetById(id) ?: return null

        val streakEntity = streakDao.getByBudgetIdFlow(id).firstOrNull() ?: return null
        val streak = streakEntity.toDomain()

        return budgetEntity.toDomain(streak)
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

        val streakEntity = streakDao.getByBudgetIdFlow(budgetEntity.id).firstOrNull() ?: return null
        val streak = streakEntity.toDomain()

        return budgetEntity.toDomain(streak)
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.update(budget.toEntity())
    }

    override fun getLatestBudgetFlow(): Flow<Budget?> {
        return budgetDao.getLatestBudgetFlow().flatMapLatest { budgetEntity ->
            if (budgetEntity == null) return@flatMapLatest flowOf(null)

            // Берем поток стрика для этого конкретного бюджета
            streakDao.getByBudgetIdFlow(budgetEntity.id).map { streakEntity ->
                val streak = streakEntity?.toDomain()
                // Передаем стрик в маппер бюджета
                budgetEntity.toDomain(streak!!)
            }
        }
    }
}