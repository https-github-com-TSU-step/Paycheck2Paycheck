package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.model.ScheduledPayment
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

data class TransactionHistory(
    val totalRemaining: Double,
    val scheduledPayments: List<ScheduledPayment>,
    val expenses: List<Expense>
)

class GetTransactionHistoryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val expenseRepository: ExpenseRepository,
    private val scheduledPaymentRepository: ScheduledPaymentRepository
) {
    operator fun invoke(): Flow<TransactionHistory> {
        return budgetRepository.getLatestBudgetFlow().flatMapLatest { budget ->
            if (budget == null) {
                return@flatMapLatest flowOf(TransactionHistory(0.0, emptyList(), emptyList()))
            }

            combine(
                flowOf(budget),
                expenseRepository.getExpensesByBudgetId(budget.id),
                scheduledPaymentRepository.getByBudgetId(budget.id)
            ) { currentBudget, expenses, scheduled ->
                TransactionHistory(
                    totalRemaining = currentBudget.remainingAmount,
                    scheduledPayments = scheduled.filter { !it.isPaid },
                    expenses = expenses.sortedByDescending { it.date }
                )
            }
        }
    }
}