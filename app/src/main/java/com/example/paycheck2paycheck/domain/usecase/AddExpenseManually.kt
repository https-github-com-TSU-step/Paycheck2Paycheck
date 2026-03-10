package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.*
import com.example.paycheck2paycheck.domain.repository.*
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class AddExpenseManually @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val expenseRepository: ExpenseRepository,
    private val streakRepository: StreakRepository,
    private val scheduledPaymentRepository: ScheduledPaymentRepository,
    private val calculateLimit: CalculateDailyLimitUseCase,
    private val updateStreak: UpdateStreakUseCase
) {
    suspend fun execute(amount: Double, description: String, budgetId: String) {
        // 1. Получаем данные
        val budget = budgetRepository.getBudgetById(budgetId) ?: throw Exception("Бюджет не найден")
        val streak = streakRepository.getStreak(budgetId) ?: throw Exception("Стрик не найден")

        // Нам нужны платежи, чтобы CalculateDailyLimitUseCase отработал корректно
        // Предположим, мы получаем их из Flow или текущего списка
        val payments = emptyList<ScheduledPayment>() // В идеале: scheduledPaymentRepository.getPaymentsOnce(budgetId)

        // 2. Создаем объект траты
        val expense = Expense(
            id = UUID.randomUUID().toString(),
            name = description,
            amount = amount,
            date = LocalDateTime.now(),
            budgetId = budget.id,
            recordMethod = RecordingMethod.MANUAL,
            createdAt = LocalDateTime.now()
        )

        // 3. Рассчитываем новое состояние (Логика больше не в моделях!)
        val newRemaining = budget.remainingAmount - amount
        val updatedBudgetSnapshot = budget.copy(remainingAmount = newRemaining)

        val newDailyLimit = calculateLimit(updatedBudgetSnapshot, payments)
        val finalBudget = updatedBudgetSnapshot.copy(
            dailyLimit = newDailyLimit,
            updatedAt = LocalDateTime.now()
        )

        val finalStreak = updateStreak(streak)

        // 4. Сохраняем всё через репозитории
        expenseRepository.addExpense(expense)
        budgetRepository.updateBudget(finalBudget)
        streakRepository.updateStreak(finalStreak)
    }
}