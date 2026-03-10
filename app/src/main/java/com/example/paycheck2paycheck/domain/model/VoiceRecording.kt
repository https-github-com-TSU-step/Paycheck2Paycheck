package com.example.paycheck2paycheck.domain.model

import java.time.LocalDateTime

data class VoiceRecording(
    val id: String,
    val audioFilePath: String,
    var transcription: String? = null,
    var recognizedAmount: Double? = null,
    var recognizedDescription: String? = null,
    var errorMessage: String? = null, // Добавлено поле для хранения текста ошибки
    var status: RecognitionStatus = RecognitionStatus.PENDING,
    val createdAt: LocalDateTime
) {
    fun startRecognition() {
        status = RecognitionStatus.PROCESSING
        errorMessage = null // Очищаем ошибку при новой попытке (если это был повтор)
    }

    fun onRecognitionSuccess(amount: Double, description: String, text: String? = null) {
        recognizedAmount = amount
        recognizedDescription = description
        transcription = text // Сохраняем сырой текст, если он пришел
        status = RecognitionStatus.SUCCESS
    }

    fun onRecognitionFailure(error: String) {
        errorMessage = error // Запоминаем причину ошибки
        status = RecognitionStatus.FAILED
    }
}
