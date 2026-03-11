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

    // 1. Был getScheduledPaymentById -> стал getById
    override suspend fun getById(id: String): ScheduledPayment? {
        return dao.getById(id)?.toDomain()
    }

    // 2. Был getScheduledPayments -> стал getByBudgetId
    override suspend fun getByBudgetId(budgetId: String): List<ScheduledPayment> {
        return dao.getByBudgetId(budgetId).map { it.toDomain() }
    }

    // 3. Был addScheduledPayment -> стал save (универсальный для добавления и обновления)
    override suspend fun save(payment: ScheduledPayment) {
        dao.insert(payment.toEntity())
    }

    // 4. Был deleteScheduledPayment -> стал delete
    override suspend fun delete(id: String) {
        dao.delete(id)
    }

    // МЕТОД markAsPaid УДАЛЯЕМ — теперь это логика Use Case через метод save()
}