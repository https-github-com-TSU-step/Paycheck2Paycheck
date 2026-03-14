package com.example.paycheck2paycheck.data.local.dao

import androidx.room.*
import com.example.paycheck2paycheck.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    suspend fun getExpenseById(id: String): ExpenseEntity?

    @Query("SELECT * FROM expenses WHERE budgetId = :budgetId ORDER BY date DESC")
    suspend fun getExpensesByBudgetId(budgetId: String): List<ExpenseEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)
}