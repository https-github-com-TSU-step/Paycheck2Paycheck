package com.example.paycheck2paycheck.data.local.dao

import androidx.room.*
import com.example.paycheck2paycheck.data.local.entity.PendingAudioEntity

@Dao
interface PendingAudioDao {
    @Query("SELECT * FROM pending_audio ORDER BY createdAt ASC")
    suspend fun getAllPending(): List<PendingAudioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audio: PendingAudioEntity)

    @Query("DELETE FROM pending_audio WHERE id = :id")
    suspend fun deleteById(id: String)
}