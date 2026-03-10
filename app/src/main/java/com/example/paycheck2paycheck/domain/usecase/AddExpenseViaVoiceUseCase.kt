package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.VoiceApi
import javax.inject.Inject

class AddExpenseViaVoiceUseCase @Inject constructor(
    private val voiceService: VoiceApi,
    private val budgetRepository: BudgetRepository
) {
    suspend fun execute(audioPath: String) {
        // Мы просто вызываем метод, не зная, что там внутри Mock или реальное API
        val result = voiceService.recognize(audioPath)

        // Дальше работаем с результатом (сумма 500, описание "кофе")
    }
}