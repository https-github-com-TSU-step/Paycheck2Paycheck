package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.ScheduledPayment

interface ScheduledPaymentRepository {
    suspend fun getScheduledPaymentById(id: String): ScheduledPayment?

    suspend fun getScheduledPayments(budgetId: String): List<ScheduledPayment>

    suspend fun addScheduledPayment(payment: ScheduledPayment)

    suspend fun markAsPaid(paymentId: String)

    suspend fun deleteScheduledPayment(paymentId: String)
}