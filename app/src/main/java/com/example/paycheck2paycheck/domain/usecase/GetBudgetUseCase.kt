package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.repository.BudgetRepository

class GetBudgetUseCase(
        private val budgetRepository: BudgetRepository
) {
    suspend fun execute(budgetId: String): Budget {
        return budgetRepository.getBudgetById(budgetId)
                ?: throw Exception("Бюджет не найден")
    }
}