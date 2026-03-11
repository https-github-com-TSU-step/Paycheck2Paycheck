package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

class CreateBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(amount: Double, startDate: LocalDateTime, endDate: LocalDateTime): String {

        val generatedId = UUID.randomUUID().toString()
        val days = ChronoUnit.DAYS.between(startDate.toLocalDate(),
            endDate.toLocalDate()).coerceAtLeast(1)
        val initialDailyLimit = amount / days
        val now = LocalDateTime.now()

        val newBudget = Budget(
            id = generatedId,
            totalAmount = amount,
            startDate = startDate,
            endDate = endDate,
            dailyLimit = initialDailyLimit,
            remainingAmount = amount,
            createdAt = now,
            updatedAt = now
        )

        budgetRepository.insertBudget(newBudget)

        return generatedId
    }
}