package com.example.paycheck2paycheck.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.paycheck2paycheck.data.local.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {

    @Query("SELECT * FROM streaks WHERE budgetId = :budgetId LIMIT 1")
    fun getByBudgetIdFlow(budgetId: String): Flow<StreakEntity?>

    @Query("SELECT * FROM streaks LIMIT 1")
    suspend fun getStreak(): StreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streak: StreakEntity)

    @Update
    suspend fun update(streak: StreakEntity)

    @Update
    suspend fun updateStreak(streak: StreakEntity)
}