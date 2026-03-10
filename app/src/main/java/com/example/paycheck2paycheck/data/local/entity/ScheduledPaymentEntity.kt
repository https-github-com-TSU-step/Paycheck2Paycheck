package com.example.paycheck2paycheck.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "scheduled_payments")
data class ScheduledPaymentEntity(
    @PrimaryKey val id: String,
    val budgetId: String,
    val amount: Double,
    val name: String,
    val date: LocalDateTime,
    val isPaid: Boolean = false
)