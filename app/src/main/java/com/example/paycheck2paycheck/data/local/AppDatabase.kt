package com.example.paycheck2paycheck.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paycheck2paycheck.data.local.converters.Converters
import com.example.paycheck2paycheck.data.local.dao.*
import com.example.paycheck2paycheck.data.local.entity.*

@Database(
    entities = [
        BudgetEntity::class,
        ExpenseEntity::class,
        ScheduledPaymentEntity::class,
        StreakEntity::class,
        PendingAudioEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun scheduledPaymentDao(): ScheduledPaymentDao
    abstract fun streakDao(): StreakDao
    abstract fun pendingAudioDao(): PendingAudioDao

    companion object {
        const val DATABASE_NAME = "paycheck_database"
    }
}