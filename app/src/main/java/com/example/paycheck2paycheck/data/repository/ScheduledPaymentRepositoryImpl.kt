package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.ScheduledPaymentDao
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.domain.model.ScheduledPayment
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import javax.inject.Inject

class ScheduledPaymentRepositoryImpl @Inject constructor(
    private val dao: ScheduledPaymentDao
) : ScheduledPaymentRepository {

    override suspend fun getScheduledPaymentById(id: String): ScheduledPayment? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getScheduledPayments(budgetId: String): List<ScheduledPayment> {
        return dao.getByBudgetId(budgetId).map { it.toDomain() }
    }

    override suspend fun addScheduledPayment(payment: ScheduledPayment) {
        dao.insert(payment.toEntity())
    }

    override suspend fun markAsPaid(paymentId: String) {
        dao.markAsPaid(paymentId)
    }

    override suspend fun deleteScheduledPayment(paymentId: String) {
        dao.delete(paymentId)
    }
}