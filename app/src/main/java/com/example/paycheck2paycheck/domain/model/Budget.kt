package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

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
        TODO()
    }

    fun getRemainingDays(): Int {
        TODO()
    }

    fun updateAfterExpense(expense: Expense) {
        TODO()
    }

    fun addScheduledPayment(payment: ScheduledPayment) {

    }

    fun isActive(): Boolean {
        TODO()
    }
}
