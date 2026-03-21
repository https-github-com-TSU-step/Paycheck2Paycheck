package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class Streak(
    val id: String,
    val budgetId: String,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastRecordedDate: LocalDateTime? = null,
    val totalDaysTracked: Int = 0
)