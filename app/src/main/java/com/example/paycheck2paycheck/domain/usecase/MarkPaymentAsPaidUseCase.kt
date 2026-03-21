package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import javax.inject.Inject

class MarkPaymentAsPaidUseCase @Inject constructor(
    private val paymentRepository: ScheduledPaymentRepository,
    private val budgetRepository: BudgetRepository,
    private val getBudget: GetBudgetUseCase
) {
    suspend fun execute(paymentId: String) {
        val payment = paymentRepository.getById(paymentId) ?: return

        val updatedPayment = payment.copy(isPaid = true)
        paymentRepository.save(updatedPayment)

        val budget = getBudget(payment.budgetId)
        val updatedBudget = budget.copy(
            remainingAmount = budget.remainingAmount - payment.amount
        )
        budgetRepository.updateBudget(updatedBudget)
    }

}