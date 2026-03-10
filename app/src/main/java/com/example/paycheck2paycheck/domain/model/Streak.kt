package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class Streak(
    val id: String,
    val budgetId: String,
    var currentStreak: Int = 0,
    var longestStreak: Int = 0,
    var lastRecordedDate: LocalDateTime? = null,
    var totalDaysTracked: Int = 0
) {
    fun recordExpenseToday() {
        // Логика проверки: была ли запись уже сегодня?
        // Если нет, увеличиваем currentStreak и обновляем lastRecordedDate
    }

    fun getStreakStatus(): StreakStatus {
        // Логика расчета статуса на основе разницы между lastRecordedDate и текущим временем
        return StreakStatus.ACTIVE
    }

    fun getDaysUntilReset(): Int {
        // Сколько часов/минут осталось до прерывания серии
        return 0
    }

    fun getMotivationalMessage(): String {
        return when (getStreakStatus()) {
            StreakStatus.ACTIVE -> "Так держать! Возможно ты перестанешь быть нищим!."
            StreakStatus.AT_RISK -> "Не забудь записать сегодняшние траты, нищук!"
            StreakStatus.BROKEN -> "Серия прервана, ты бездарность, но начать заново никогда не поздно."
        }
    }
}
