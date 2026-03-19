package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.ScheduledPayment
import kotlinx.coroutines.flow.Flow

interface ScheduledPaymentRepository {
    // 1. Получить один (для деталей)
    suspend fun getById(id: String): ScheduledPayment?

    // 2. Получить список (для расчета бюджета и отображения)
    fun getByBudgetId(budgetId: String): Flow<List<ScheduledPayment>>

    // 3. Сохранить или Обновить (Универсальный метод)
    suspend fun save(payment: ScheduledPayment)

    // 4. Удалить
    suspend fun delete(id: String)
}