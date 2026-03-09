package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class VoiceRecording(
    val id: String,
    val audioFilePath: String,
    var transcription: String? = null,
    var recognizedAmount: Double? = null,
    var recognizedDescription: String? = null,
    var status: RecognitionStatus = RecognitionStatus.PENDING,
    val createdAt: LocalDateTime
) {
    fun startRecognition() {
        // TODO()
        status = RecognitionStatus.PROCESSING
    }

    fun onRecognitionSuccess(amount: Double, description: String) {
        // TODO()
        recognizedAmount = amount
        recognizedDescription = description
        status = RecognitionStatus.SUCCESS
    }

    fun onRecognitionFailure(error: String) {
        // TODO()
        status = RecognitionStatus.FAILED
    }
}
