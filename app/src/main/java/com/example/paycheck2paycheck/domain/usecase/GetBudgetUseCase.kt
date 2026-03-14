package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budgetId: String): Budget {
        return budgetRepository.getBudgetById(budgetId)
            ?: throw Exception("Бюджет не найден")
    }
}