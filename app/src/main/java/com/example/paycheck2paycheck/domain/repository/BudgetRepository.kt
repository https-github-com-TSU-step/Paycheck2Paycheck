package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    suspend fun getBudgetById(id: String): Budget?
    suspend fun getLatestBudget(): Budget?
    suspend fun insertBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
    fun getLatestBudgetFlow(): Flow<Budget?>
}
