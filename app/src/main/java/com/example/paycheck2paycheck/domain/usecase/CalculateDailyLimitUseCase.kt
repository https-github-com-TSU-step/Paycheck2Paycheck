package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class CalculateDailyLimitUseCase(
    private val budgetRepository: BudgetRepository,
    private val scheduledPaymentRepository: ScheduledPaymentRepository
) {
    suspend fun execute(budgetId: String): Double {

        // Получение бюджета из БД
        val budget = budgetRepository.getBudgetById(budgetId)
            ?: throw Exception("Бюджет не найден")

        // Получение неоплаченных будущих платежей
        val scheduledPayments = scheduledPaymentRepository
            .getScheduledPayments(budgetId)
            .filter { !it.isPaid && it.date.isAfter(LocalDateTime.now()) }

        // Сумма будущих платежей
        val futurePaymentsSum = scheduledPayments.sumOf { it.amount }

        // Доступная сумма = остаток - будущие платежи
        val availableAmount = budget.remainingAmount - futurePaymentsSum

        // Оставшиеся дни до конца периода
        val remainingDays = ChronoUnit.DAYS.between(
            LocalDateTime.now(),
            budget.endDate
        ).toInt()

        // Дневной лимит
        return if (remainingDays > 0) {
            availableAmount / remainingDays
        } else {
            0.0
        }
    }
}