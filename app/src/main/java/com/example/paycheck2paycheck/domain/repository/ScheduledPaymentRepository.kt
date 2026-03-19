package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.ScheduledPayment
import kotlinx.coroutines.flow.Flow

interface ScheduledPaymentRepository {
    suspend fun getById(id: String): ScheduledPayment?

    fun getByBudgetId(budgetId: String): Flow<List<ScheduledPayment>>

    suspend fun save(payment: ScheduledPayment)

    suspend fun delete(id: String)
}