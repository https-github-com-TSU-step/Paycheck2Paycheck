package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetUseCase @Inject constructor(  // ← ДОБАВЬ @Inject
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budgetId: String): Budget {  // ← operator для вызова через ()
        return budgetRepository.getBudgetById(budgetId)
            ?: throw Exception("Бюджет не найден")
    }
}