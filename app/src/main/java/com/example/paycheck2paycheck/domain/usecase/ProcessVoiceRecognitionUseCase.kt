package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.RecognitionStatus
import com.example.paycheck2paycheck.domain.model.VoiceRecording
import com.example.paycheck2paycheck.domain.repository.VoiceApi
import com.example.paycheck2paycheck.domain.repository.VoiceRepository

class ProcessVoiceRecognitionUseCase(
    private val voiceRepository: VoiceRepository,
    private val voiceApi: VoiceApi
) {
    suspend fun execute(recording: VoiceRecording) {
        // 1. Переходим в статус обработки
        val processingRecord = recording.copy(status = RecognitionStatus.PROCESSING, errorMessage = null)
        voiceRepository.saveAudioToQueue(processingRecord)

        try {
            val result = voiceApi.recognize(processingRecord.audioFilePath)
            // 2. Успех - создаем новую копию с данными
            val successRecord = processingRecord.copy(
                recognizedAmount = result.amount,
                recognizedDescription = result.description,
                status = RecognitionStatus.SUCCESS
            )
            voiceRepository.saveAudioToQueue(successRecord)
        } catch (e: Exception) {
            // 3. Ошибка
            val errorRecord = processingRecord.copy(
                status = RecognitionStatus.FAILED,
                errorMessage = e.message
            )
            voiceRepository.saveAudioToQueue(errorRecord)
        }
    }
}