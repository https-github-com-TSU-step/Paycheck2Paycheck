package com.example.paycheck2paycheck.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val totalAmount: Double,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val dailyLimit: Double,
    val remainingAmount: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)