package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class Expense(
    override val id: String,
    override val name: String,
    override val amount: Double,
    override val date: LocalDateTime,
    override val budgetId: String,
    val recordMethod: RecordingMethod,
    val createdAt: LocalDateTime,
    val voiceRecordingId: String? = null // Ссылка на голосовую запись (если создано голосом)
) : MoneyOperation()
