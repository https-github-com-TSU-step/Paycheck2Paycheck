package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class ApplyExpenseToBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val expenseRepository: ExpenseRepository,
    private val calcDailyLimit : CalculateDailyLimitUseCase,
    private val getExpensesForDayUseCase: GetExpensesForDayUseCase,
    private val updateStreakUseCase : UpdateStreakUseCase
) {
    suspend fun applyExpense(expense: Expense, budget: Budget){
        val now = LocalDate.now()

        val todayExpenses = getExpensesForDayUseCase.getExpensesForDay(budget.id, now)
        val todaySpent = todayExpenses.sumOf { it.amount }

        println("DEBUG: budget.id=${budget.id}")
        println("DEBUG: expense.amount=${expense.amount}")
        println("DEBUG: todaySpent=$todaySpent")
        println("DEBUG: budget.dailyLimit=${budget.dailyLimit}")
        println("DEBUG: budget.remainingAmount=${budget.remainingAmount}")

        val isLimitSum = (todaySpent + expense.amount) > budget.dailyLimit
        val isAllLimitSum = expense.amount > budget.remainingAmount
        val newRemaining = if (isAllLimitSum) {
            0.0
        } else {
            budget.remainingAmount - expense.amount
        }
        println("DEBUG: isLimitSum=$isLimitSum, isAllLimitSum=$isAllLimitSum, newRemaining=$newRemaining")

        val newDailyLimit = if (isLimitSum){
            val newBudget = budget.copy(
                remainingAmount = newRemaining
            )
            calcDailyLimit(newBudget).also { println("DEBUG: recalculated dailyLimit = $it") }
        }else {
            println("DEBUG: keeping old dailyLimit = ${budget.dailyLimit}")
            budget.dailyLimit
        }
        val newCurDayBudget = if(budget.dailyLimit == newDailyLimit){
            budget.curDayBudget - expense.amount
        }else{
            0.0
        }


        val updatedBudget = budget.copy(
            remainingAmount = newRemaining,
            dailyLimit = newDailyLimit,
            updatedAt = LocalDateTime.now(),
            curDayBudget = newCurDayBudget,
        )
        println("DEBUG: updatedBudget = $updatedBudget")
        expenseRepository.addExpense(expense)
        budgetRepository.updateBudget(updatedBudget)

        updateStreakUseCase(budget.id, isLimitSum)
    }

}