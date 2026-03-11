package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.model.RecordingMethod
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import com.example.paycheck2paycheck.domain.repository.VoiceRepository
import java.io.File
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import kotlin.math.max

class AddExpenseViaVoiceUseCase @Inject constructor(
    private val voiceRepository: VoiceRepository,
    private val expenseRepository: ExpenseRepository,
    private val budgetRepository: BudgetRepository,
    private val scheduledPaymentRepository: ScheduledPaymentRepository,
    private val updateStreakUseCase: UpdateStreakUseCase
) {
    suspend fun execute(audioFile: File, budgetId: String) {
        // 1. Распознать голос
        val result = voiceRepository.recognizeVoice(audioFile)

        val budget = budgetRepository.getBudgetById(budgetId)
            ?: throw Exception("Бюджет не найден")

        // 2. Создать expense
        val expense = Expense(
            id = UUID.randomUUID().toString(),
            name = result.description,
            amount = result.amount,
            date = LocalDateTime.now(),
            budgetId = budget.id,
            recordMethod = RecordingMethod.VOICE,
            createdAt = LocalDateTime.now()
        )

        // 3. Обновить budget
        val newRemaining = budget.remainingAmount - result.amount

        // 4. Пересчитать лимит
        val scheduledPayments = scheduledPaymentRepository.getByBudgetId(budgetId)
        val unpaidAmount = scheduledPayments.filter { !it.isPaid }.sumOf { it.amount }
        val daysLeft = getRemainingDays(budget.startDate, budget.endDate)
        val availableFunds = newRemaining - unpaidAmount
        val newDailyLimit = if (daysLeft > 0) availableFunds / daysLeft else availableFunds

        val updatedBudget = budget.copy(
            remainingAmount = newRemaining,
            dailyLimit = newDailyLimit,
            updatedAt = LocalDateTime.now()
        )

        // 5. Сохранить
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