package com.example.paycheck2paycheck.data.local.dao

import androidx.room.*
import com.example.paycheck2paycheck.data.local.entity.StreakEntity

@Dao
interface StreakDao {

    @Query("SELECT * FROM streaks WHERE budgetId = :budgetId LIMIT 1")
    suspend fun getByBudgetId(budgetId: String): StreakEntity?

    @Query("SELECT * FROM streaks LIMIT 1")
    suspend fun getStreak(): StreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streak: StreakEntity)

    @Update
    suspend fun update(streak: StreakEntity)

    @Update
    suspend fun updateStreak(streak: StreakEntity)
}