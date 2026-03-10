package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class ScheduledPayment(
    val id: String,
    val name: String,
    val amount: Double,
    val date: LocalDateTime,
    val budgetId: String,
    val isPaid: Boolean = false // Теперь val
)
