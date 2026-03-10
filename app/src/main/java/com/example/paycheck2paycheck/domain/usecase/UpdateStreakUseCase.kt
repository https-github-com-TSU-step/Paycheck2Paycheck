package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Streak
import java.time.LocalDateTime

class UpdateStreakUseCase {
    operator fun invoke(currentStreak: Streak): Streak {
        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val lastRecordDay = currentStreak.lastRecordedDate?.toLocalDate()

        // Если сегодня уже записывали — ничего не меняем
        if (lastRecordDay == today) return currentStreak

        val newCurrentStreak = if (lastRecordDay == today.minusDays(1)) {
            currentStreak.currentStreak + 1
        } else {
            1
        }

        val newLongest = if (newCurrentStreak > currentStreak.longestStreak) {
            newCurrentStreak
        } else {
            currentStreak.longestStreak
        }

        return currentStreak.copy(
            currentStreak = newCurrentStreak,
            longestStreak = newLongest,
            totalDaysTracked = currentStreak.totalDaysTracked + 1,
            lastRecordedDate = now
        )
    }
}