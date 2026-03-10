package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.StreakDao
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.domain.model.Streak
import com.example.paycheck2paycheck.domain.repository.StreakRepository
import javax.inject.Inject

class StreakRepositoryImpl @Inject constructor(
    private val streakDao: StreakDao
) : StreakRepository {

    override suspend fun getStreak(): Streak? {
        // Берем Entity из БД и превращаем в доменную модель через маппер
        return streakDao.getStreak()?.toDomain()
    }

    override suspend fun updateStreak(streak: Streak) {
        // Превращаем доменную модель в Entity и сохраняем в БД
        streakDao.updateStreak(streak.toEntity())
    }
}