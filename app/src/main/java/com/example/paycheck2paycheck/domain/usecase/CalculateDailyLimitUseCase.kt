package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.model.ScheduledPayment
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.max

class CalculateDailyLimitUseCase {

    operator fun invoke(budget: Budget, scheduledPayments: List<ScheduledPayment>): Double {
        val daysLeft = getRemainingDays(budget.startDate, budget.endDate)

        // Считаем только невыплаченные запланированные платежи
        val unpaidAmount = scheduledPayments
            .filter { !it.isPaid }
            .sumOf { it.amount }

        val availableFunds = budget.remainingAmount - unpaidAmount

        return if (daysLeft > 0) {
            availableFunds / daysLeft
        } else {
            availableFunds
        }
    }

    private fun getRemainingDays(startDate: LocalDateTime, endDate: LocalDateTime): Int {
        val now = LocalDateTime.now()
        if (now.isAfter(endDate)) return 0
        if (now.isBefore(startDate)) {
            return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()).toInt()
        }
        val days = ChronoUnit.DAYS.between(now.toLocalDate(), endDate.toLocalDate()).toInt()
        return max(1, days)
    }
}