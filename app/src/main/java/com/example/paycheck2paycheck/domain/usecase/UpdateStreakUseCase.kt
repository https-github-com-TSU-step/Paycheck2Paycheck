package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Streak
import com.example.paycheck2paycheck.domain.repository.StreakRepository
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateStreakUseCase @Inject constructor(
    private val streakRepository: StreakRepository
) {
    suspend operator fun invoke(budgetId: String) {
        // 1. Получить текущий streak из Repository
        val currentStreak = streakRepository.getStreak(budgetId)
            ?: throw Exception("Streak не найден")

        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val lastRecordDay = currentStreak.lastRecordedDate?.toLocalDate()

        // 2. Если сегодня уже записывали — ничего не делаем
        if (lastRecordDay == today) return

        // 3. Вычисляем новые значения
        val newCurrentStreak = if (lastRecordDay == today.minusDays(1)) {
            currentStreak.currentStreak + 1
        } else {
            1
        }

        val newLongest = maxOf(newCurrentStreak, currentStreak.longestStreak)

        // 4. Создаём обновлённый streak через copy
        val updatedStreak = currentStreak.copy(
            currentStreak = newCurrentStreak,
            longestStreak = newLongest,
            totalDaysTracked = currentStreak.totalDaysTracked + 1,
            lastRecordedDate = now
        )

        // 5. Сохраняем в Repository
        streakRepository.updateStreak(updatedStreak)
    }
}