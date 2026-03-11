package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class Budget(
    val id: String,
    val totalAmount: Double,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val dailyLimit: Double,
    val remainingAmount: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)