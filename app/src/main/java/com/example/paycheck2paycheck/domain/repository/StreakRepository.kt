package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.Streak // Предполагаем, что модель есть

interface StreakRepository {
    suspend fun getStreak(budgetId: String): Streak?
    suspend fun updateStreak(streak: Streak)
}