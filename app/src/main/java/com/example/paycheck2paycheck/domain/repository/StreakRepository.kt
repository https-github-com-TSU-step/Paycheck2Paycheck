package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.Streak // Предполагаем, что модель есть
import kotlinx.coroutines.flow.Flow

interface StreakRepository {
    suspend fun getStreak(): Streak?
    suspend fun updateStreak(streak: Streak)
}