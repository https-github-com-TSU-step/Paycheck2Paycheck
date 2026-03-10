package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

sealed class MoneyOperation {
    abstract val id: String
    abstract val name: String
    abstract val amount: Double
    abstract val date: LocalDateTime
    abstract val budgetId: String
}