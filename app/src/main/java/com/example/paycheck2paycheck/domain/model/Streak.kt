package com.example.paycheck2paycheck.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

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
        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val lastRecordDay = lastRecordedDate?.toLocalDate()
        if (lastRecordDay == today) {
            return
        }
        if (lastRecordDay == today.minusDays(1)) {
            currentStreak++
        } else {
            currentStreak = 1
        }

        if (currentStreak > longestStreak) {
            longestStreak = currentStreak
        }

        totalDaysTracked++
        lastRecordedDate = now
    }

    fun getStreakStatus(): StreakStatus {
        // Логика расчета статуса на основе разницы между lastRecordedDate и текущим временем
        val lastRecordDay = lastRecordedDate?.toLocalDate() ?: return StreakStatus.BROKEN
        val today = LocalDate.now()

        if(lastRecordDay == today) {
            return StreakStatus.ACTIVE
        }else if(lastRecordDay == today.minusDays(1)){
            return StreakStatus.AT_RISK
        }else{
            return StreakStatus.BROKEN
        }
    }

    fun getDaysUntilReset(): Int {
        // Сколько часов/минут осталось до прерывания серии
        val status = getStreakStatus()
        if (status == StreakStatus.BROKEN) return 0

        val now = LocalDateTime.now()

        val targetDate = if (status == StreakStatus.ACTIVE) {
            now.toLocalDate().plusDays(1)
        }else{
            now.toLocalDate()
        }
        val endOfTargetDay = targetDate.atTime(23, 59, 59)

        return ChronoUnit.HOURS.between(now, endOfTargetDay).toInt()
    }

    fun getMotivationalMessage(): String {
        // Вывод сообщений в зависимости от стрика
        if(getStreakStatus() == StreakStatus.ACTIVE){
            return "Так держать! Возможно ты перестанешь быть нищим!."
        }else if(getStreakStatus() == StreakStatus.AT_RISK){
            return "Не забудь записать сегодняшние траты, нищук!"
        }else{
            return "Серия прервана, ты бездарность, но начать заново никогда не поздно."
        }
    }
}
