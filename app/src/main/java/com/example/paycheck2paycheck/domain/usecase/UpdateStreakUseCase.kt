package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.repository.StreakRepository
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateStreakUseCase @Inject constructor(
    private val streakRepository: StreakRepository
) {
    suspend operator fun invoke(budgetId: String, isLimitSum: Boolean) {
        val curStreak = streakRepository.getStreak(budgetId)
            ?: throw Exception("Streak не найден")

        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val lastRecordDay = curStreak.lastRecordedDate?.toLocalDate()

        val newCurStreak = if ( isLimitSum){
            0
        }else if (lastRecordDay == today){
            curStreak.currentStreak
        }else if(lastRecordDay == today.minusDays(1)){
            curStreak.currentStreak + 1
        }
        else {
            1
        }

        val newLongest = maxOf(newCurStreak, curStreak.longestStreak)

        val newTotalDaysTracked = if (lastRecordDay != today) {
            curStreak.totalDaysTracked + 1
        } else {
            curStreak.totalDaysTracked
        }

        val updatedStreak = curStreak.copy(
            currentStreak = newCurStreak,
            longestStreak = newLongest,
            totalDaysTracked = newTotalDaysTracked,
            lastRecordedDate = now
        )
        streakRepository.updateStreak(updatedStreak)

    }
}