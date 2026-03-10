package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class VoiceRecording(
    val id: String,
    val audioFilePath: String,
    val transcription: String? = null,
    val recognizedAmount: Double? = null,
    val recognizedDescription: String? = null,
    val errorMessage: String? = null,
    val status: RecognitionStatus = RecognitionStatus.PENDING,
    val createdAt: LocalDateTime
)