package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.*
import com.example.paycheck2paycheck.domain.repository.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import kotlin.math.max

class AddExpenseManually @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val expenseRepository: ExpenseRepository,
    private val scheduledPaymentRepository: ScheduledPaymentRepository,
    private val updateStreakUseCase: UpdateStreakUseCase
) {
    suspend fun execute(amount: Double, description: String, budgetId: String) {
        val budget = budgetRepository.getBudgetById(budgetId)
            ?: throw Exception("Бюджет не найден")

        val expense = Expense(
            id = UUID.randomUUID().toString(),
            name = description,
            amount = amount,
            date = LocalDateTime.now(),
            budgetId = budget.id,
            recordMethod = RecordingMethod.MANUAL,
            createdAt = LocalDateTime.now()
        )

        // Новый remainingAmount
        val newRemaining = budget.remainingAmount - amount

        // Расчёт нового лимита напрямую здесь
        val scheduledPayments = scheduledPaymentRepository.getByBudgetId(budgetId)
        val unpaidAmount = scheduledPayments.filter { !it.isPaid }.sumOf { it.amount }
        val daysLeft = getRemainingDays(budget.startDate, budget.endDate)
        val availableFunds = newRemaining - unpaidAmount
        val newDailyLimit = if (daysLeft > 0) availableFunds / daysLeft else availableFunds

        // Обновить budget одним UPDATE
        val updatedBudget = budget.copy(
            remainingAmount = newRemaining,
            dailyLimit = newDailyLimit,
            updatedAt = LocalDateTime.now()
        )

        expenseRepository.addExpense(expense)
        budgetRepository.updateBudget(updatedBudget)
        updateStreakUseCase(budgetId)
    }

    private fun getRemainingDays(startDate: LocalDateTime, endDate: LocalDateTime): Int {
        val now = LocalDateTime.now()
        if (now.isAfter(endDate)) return 0
        if (now.isBefore(startDate)) {
            return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()).toInt()
        }
        return max(1, ChronoUnit.DAYS.between(now.toLocalDate(), endDate.toLocalDate()).toInt())
    }
}