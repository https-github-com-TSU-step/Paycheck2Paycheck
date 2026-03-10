package com.example.paycheck2paycheck.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "pending_audio")
data class PendingAudioEntity(
    @PrimaryKey val id: String,
    val audioFilePath: String,
    val transcription: String?,
    val recognizedAmount: Double?,
    val recognizedDescription: String?,
    val status: String, // Храним RecognitionStatus.name
    val errorMessage: String?,
    val createdAt: LocalDateTime
)