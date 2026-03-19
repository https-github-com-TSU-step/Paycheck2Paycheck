package com.example.paycheck2paycheck.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paycheck2paycheck.data.local.entity.ScheduledPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledPaymentDao {
    @Query("SELECT * FROM scheduled_payments WHERE id = :id")
    suspend fun getById(id: String): ScheduledPaymentEntity?

    @Query("SELECT * FROM scheduled_payments WHERE budgetId = :budgetId")
    fun getByBudgetId(budgetId: String): Flow<List<ScheduledPaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: ScheduledPaymentEntity)

    @Query("UPDATE scheduled_payments SET isPaid = 1 WHERE id = :paymentId")
    suspend fun markAsPaid(paymentId: String)

    @Query("DELETE FROM scheduled_payments WHERE id = :paymentId")
    suspend fun delete(paymentId: String)
}