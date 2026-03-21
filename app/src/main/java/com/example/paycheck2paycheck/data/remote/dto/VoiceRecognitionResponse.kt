package com.example.paycheck2paycheck.data.remote.dto

data class VoiceRecognitionResponse(
    val amount: Double,
    val description: String,
    val success: Boolean
)