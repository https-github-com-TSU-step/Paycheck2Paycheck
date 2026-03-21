package com.example.paycheck2paycheck.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val budgetId: String,
    val amount: Double,
    val description: String,
    val date: LocalDateTime,
    val recordingMethod: String,
    val createdAt: LocalDateTime
)