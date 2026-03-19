package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.ScheduledPayment

interface ScheduledPaymentRepository {
    suspend fun getById(id: String): ScheduledPayment?

    suspend fun getByBudgetId(budgetId: String): List<ScheduledPayment>

    suspend fun save(payment: ScheduledPayment)

    suspend fun delete(id: String)
}