package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class ScheduledPayment(
    override val id: String,
    override val name: String,
    override val amount: Double,
    override val date: LocalDateTime,
    override val budgetId: String,
    val isPaid: Boolean = false // Теперь val
) : MoneyOperation()
