package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.model.RecordingMethod
import com.example.paycheck2paycheck.domain.repository.VoiceRepository
import java.io.File
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class AddExpenseViaVoiceUseCase @Inject constructor(
    private val voiceRepository: VoiceRepository,
    private val applyExpenseToBudgetUseCase: ApplyExpenseToBudgetUseCase,
    private val getBudget: GetBudgetUseCase
) {
    suspend fun execute(audioFile: File, budgetId: String) {
        val result = voiceRepository.recognizeVoice(audioFile)
        val budget: Budget = getBudget(budgetId)

        val expense = Expense(
            id = UUID.randomUUID().toString(),
            name = result.description,
            amount = result.amount,
            date = LocalDateTime.now(),
            budgetId = budget.id,
            recordMethod = RecordingMethod.VOICE,
            createdAt = LocalDateTime.now()
        )
        applyExpenseToBudgetUseCase.applyExpense(expense, budget)
    }
}