package com.example.paycheck2paycheck.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.paycheck2paycheck.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: String): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity)

    @Update
    suspend fun update(budget: BudgetEntity)

    @Query("SELECT * FROM budgets ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestBudget(): BudgetEntity?

    @Query("SELECT * FROM budgets ORDER BY createdAt DESC LIMIT 1")
    fun getLatestBudgetFlow(): Flow<BudgetEntity?>
}