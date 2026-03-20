package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateExpenseInBudgetUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val budgetRepository: BudgetRepository,
    private val calcDailyLimit: CalculateDailyLimitUseCase,
    private val getExpensesForDayUseCase: GetExpensesForDayUseCase,
    private val updateStreakUseCase: UpdateStreakUseCase
) {
    suspend fun updateExpense(expenseId: String, newAmount: Double, newDescription: String, budgetId: String) {
        val expense = expenseRepository.getExpenseById(expenseId)
            ?: throw Exception("Трата не найдена")

        val budget = budgetRepository.getBudgetById(budgetId)
            ?: throw Exception("Бюджет не найден")

        val difference = newAmount - expense.amount

        val isAllLimitSum = difference > budget.remainingAmount
        val newRemaining = maxOf(0.0, budget.remainingAmount - difference)

        val now = LocalDate.now()
        val todayExpenses = getExpensesForDayUseCase.getExpensesForDay(budget.id, now)

        val todaySpent = todayExpenses.sumOf {
            if (it.id == expenseId) newAmount else it.amount
        }
        val isDailyLimitExceeded = todaySpent > budget.dailyLimit

        val newDailyLimit = if (isAllLimitSum) {
            0.0
        } else if (isDailyLimitExceeded || difference != 0.0) {
            calcDailyLimit(budget.copy(remainingAmount = newRemaining))
        } else {
            budget.dailyLimit
        }

        val updatedBudget = budget.copy(
            remainingAmount = newRemaining,
            dailyLimit = newDailyLimit,
            updatedAt = LocalDateTime.now()
        )

        val updatedExpense = expense.copy(
            amount = newAmount,
            name = newDescription
        )

        expenseRepository.updateExpense(updatedExpense)
        budgetRepository.updateBudget(updatedBudget)

        updateStreakUseCase(budget.id, isDailyLimitExceeded)
    }
}