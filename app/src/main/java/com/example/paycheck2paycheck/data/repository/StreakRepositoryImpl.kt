package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.StreakDao
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.domain.model.Streak
import com.example.paycheck2paycheck.domain.repository.StreakRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class StreakRepositoryImpl @Inject constructor(
    private val streakDao: StreakDao
) : StreakRepository {

    override suspend fun getStreak(budgetId: String): Streak? {
        return streakDao.getByBudgetIdFlow(budgetId)
            .firstOrNull()
            ?.toDomain()
    }

    override suspend fun updateStreak(streak: Streak) {
        // Превращаем доменную модель в Entity и сохраняем в БД
        streakDao.updateStreak(streak.toEntity())
    }
}