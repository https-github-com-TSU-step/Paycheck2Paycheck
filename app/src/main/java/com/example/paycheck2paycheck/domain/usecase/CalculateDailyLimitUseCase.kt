package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.max

class CalculateDailyLimitUseCase @Inject constructor(
    private val scheduledPaymentRepository: ScheduledPaymentRepository
) {
    suspend operator fun invoke(budget: Budget): Double {

        val scheduledPayments = scheduledPaymentRepository.getByBudgetId(budget.id)

        val daysLeft = getRemainingDays(budget.startDate, budget.endDate)

        val unpaidAmount = scheduledPayments
            .filter { !it.isPaid }
            .sumOf { it.amount }

        val availableFunds = budget.remainingAmount - unpaidAmount

        return if (daysLeft > 0) availableFunds / daysLeft else availableFunds
    }
    private fun getRemainingDays(st: LocalDateTime, end: LocalDateTime): Int {
        val now = LocalDateTime.now()
        if (now > end) return 0
        else if (now < st) return ChronoUnit.DAYS.between(st.toLocalDate(), end.toLocalDate()).toInt()
        else return max(1, ChronoUnit.DAYS.between(now.toLocalDate(), end.toLocalDate()).toInt())
    }
}