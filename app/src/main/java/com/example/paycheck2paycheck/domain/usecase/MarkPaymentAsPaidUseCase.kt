package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.max

class MarkPaymentAsPaidUseCase @Inject constructor(
    private val paymentRepository: ScheduledPaymentRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend fun execute(paymentId: String) {
        val payment = paymentRepository.getById(paymentId) ?: return

        // Обновляем статус
        val updatedPayment = payment.copy(isPaid = true)
        paymentRepository.save(updatedPayment)

        // Получаем бюджет
        val budget = budgetRepository.getBudgetById(payment.budgetId) ?: return

        // Пересчитываем лимит напрямую
        val allPayments = paymentRepository.getByBudgetId(budget.id)
        val unpaidAmount = allPayments.filter { !it.isPaid }.sumOf { it.amount }
        val daysLeft = getRemainingDays(budget.startDate, budget.endDate)
        val availableFunds = budget.remainingAmount - unpaidAmount
        val newDailyLimit = if (daysLeft > 0) availableFunds / daysLeft else availableFunds

        // Обновляем бюджет
        budgetRepository.updateBudget(
            budget.copy(dailyLimit = newDailyLimit)
        )
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