package com.example.paycheck2paycheck.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "streaks")
data class StreakEntity(
    @PrimaryKey val id: String,
    val budgetId: String,          // Добавлено для привязки к бюджету
    val currentStreak: Int,
    val longestStreak: Int,        // В домене у тебя 'longestStreak', в первой версии Entity было 'maxStreak'
    val lastRecordedDate: LocalDateTime?, // Делаем nullable, так как в домене он может быть null
    val totalDaysTracked: Int
)