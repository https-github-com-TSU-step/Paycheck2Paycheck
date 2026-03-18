package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.model.RecordingMethod
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import kotlin.math.max

class AddExpenseManually @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val applyExpenseToBudgetUseCase: ApplyExpenseToBudgetUseCase,
    private val getBudget: GetBudgetUseCase
) {
    suspend fun execute(amount: Double, description: String, budgetId: String) {
        val budget: Budget = getBudget(budgetId)

        val expense = Expense(
            id = UUID.randomUUID().toString(),
            name = description,
            amount = amount,
            date = LocalDateTime.now(),
            budgetId = budget.id,
            recordMethod = RecordingMethod.MANUAL,
            createdAt = LocalDateTime.now()
        )

        applyExpenseToBudgetUseCase.applyExpense(expense, budget)
    }
}