package com.example.paycheck2paycheck.data.local.dao

import androidx.room.*
import com.example.paycheck2paycheck.data.local.entity.ScheduledPaymentEntity

@Dao
interface ScheduledPaymentDao {
    @Query("SELECT * FROM scheduled_payments WHERE id = :id")
    suspend fun getById(id: String): ScheduledPaymentEntity?

    @Query("SELECT * FROM scheduled_payments WHERE budgetId = :budgetId")
    suspend fun getByBudgetId(budgetId: String): List<ScheduledPaymentEntity>  // ← ДОБАВЬ

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: ScheduledPaymentEntity)

    @Query("UPDATE scheduled_payments SET isPaid = 1 WHERE id = :paymentId")
    suspend fun markAsPaid(paymentId: String)

    @Query("DELETE FROM scheduled_payments WHERE id = :paymentId")
    suspend fun delete(paymentId: String)
}