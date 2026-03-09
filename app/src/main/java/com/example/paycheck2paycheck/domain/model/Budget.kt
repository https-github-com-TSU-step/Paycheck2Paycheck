package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.max

data class Budget(
    val id: String,
    val totalAmount: Double,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    var dailyLimit: Double,
    var remainingAmount: Double,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    val expenses: MutableList<Expense> = mutableListOf<Expense>(),
    val scheduledPayments: MutableList<ScheduledPayment> = mutableListOf<ScheduledPayment>(),
    var streak: Streak
) {

    fun calculateDailyLimit(): Double {
        val daysLeft = getRemainingDays()
        val unpaidPayments = scheduledPayments.filter { !it.isPaid }.sumOf { it.amount }
        val avalibleFunds = remainingAmount - unpaidPayments

        dailyLimit = if (daysLeft > 0) {
            avalibleFunds / daysLeft
        } else { avalibleFunds}

        return dailyLimit
    }

    fun getRemainingDays(): Int {
        val now = LocalDateTime.now()

        if (now.isAfter(endDate)) return 0
        if (now.isBefore(startDate)) {
            return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()).toInt()
        }

        val days = ChronoUnit.DAYS.between(now.toLocalDate(), endDate.toLocalDate()).toInt()

        return max(1, days)
    }

    fun updateAfterExpense(expense: Expense) {
        expenses.add(expense)
        remainingAmount -= expense.amount
        updatedAt = LocalDateTime.now()

        streak.recordExpenseToday()
        calculateDailyLimit()
    }

    fun addScheduledPayment(payment: ScheduledPayment) {
        scheduledPayments.add(payment)
        calculateDailyLimit()
    }

    fun isActive(): Boolean {
        val now = LocalDateTime.now()
        return !now.isBefore(startDate) && !now.isAfter(endDate)
    }
}
