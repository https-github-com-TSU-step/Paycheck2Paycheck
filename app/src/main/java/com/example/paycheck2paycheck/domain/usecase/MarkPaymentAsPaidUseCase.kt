package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import javax.inject.Inject

class MarkPaymentAsPaidUseCase @Inject constructor(
    private val paymentRepository: ScheduledPaymentRepository,
    private val budgetRepository: BudgetRepository,
    private val calculateLimit: CalculateDailyLimitUseCase
) {
    suspend fun execute(paymentId: String) {
        // 1. Получаем сам платеж
        val payment = paymentRepository.getById(paymentId) ?: return

        // 2. Обновляем статус платежа в базе
        val updatedPayment = payment.copy(isPaid = true)
        paymentRepository.save(updatedPayment)

        // 3. Получаем актуальный бюджет
        val budget = budgetRepository.getBudgetById(payment.budgetId) ?: return

        // 4. Получаем ВСЕ платежи этого бюджета, чтобы расчет лимита был точным
        // (ведь один стал оплаченным, значит "замороженных" денег стало меньше)
        val allPayments = paymentRepository.getByBudgetId(budget.id)

        // 5. Вызываем наш математический Use Case (вот он, расчет лимита)
        val newDailyLimit = calculateLimit(budget, allPayments)

        // 6. Сохраняем обновленный бюджет с новым лимитом
        budgetRepository.updateBudget(
            budget.copy(dailyLimit = newDailyLimit)
        )
    }
}