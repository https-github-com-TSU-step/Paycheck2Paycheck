package com.example.paycheck2paycheck.data.local.dao

import androidx.room.*
import com.example.paycheck2paycheck.data.local.entity.BudgetEntity

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
}