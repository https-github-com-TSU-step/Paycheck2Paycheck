package com.example.paycheck2paycheck.domain.repository

interface VoiceApi {
    suspend fun recognize(audioPath: String): VoiceResult
}

data class VoiceResult(
    val amount: Double?,
    val description: String?,
    val rawText: String? = null
)