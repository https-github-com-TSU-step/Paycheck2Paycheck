package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.model.RecordingMethod
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import java.time.LocalDateTime
import java.util.UUID

class AddExpenseManually (
    private val budgetRepository: BudgetRepository,
    private val expenseRepository: ExpenseRepository
) {
    // Функция, которую вызовет кнопка ui
    suspend fun execute(amount: Double, description: String, budgetId: String) {

        // Получение бюджета из бд
        val budget = budgetRepository.getBudgetById(budgetId)
            ?: throw Exception("Бюджет не найден")

        // Формирование расхода из ввода юзера
        val expense = Expense(
            id = UUID.randomUUID().toString(),
            name = description,
            amount = amount,
            date = LocalDateTime.now(),
            budgetId = budget.id,
            recordMethod = RecordingMethod.MANUAL,
            createdAt = LocalDateTime.now()
        )
        budget.updateAfterExpense(expense)

        expenseRepository.addExpense(expense)
        budgetRepository.updateBudget(budget)
    }
}