package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.RecognitionStatus
import com.example.paycheck2paycheck.domain.model.VoiceRecording
import com.example.paycheck2paycheck.domain.repository.VoiceRepository
import java.io.File
import javax.inject.Inject

class ProcessVoiceRecognitionUseCase @Inject constructor(  // ← ДОБАВЬ @Inject
    private val voiceRepository: VoiceRepository
    // ← УБРАЛ VoiceApi, он внутри VoiceRepository
) {
    suspend operator fun invoke(recording: VoiceRecording) {  // ← operator fun invoke
        // 1. Переходим в статус обработки
        val processingRecord = recording.copy(
            status = RecognitionStatus.PROCESSING,
            errorMessage = null
        )
        voiceRepository.saveAudioToQueue(processingRecord)

        try {
            // 2. Распознаём через VoiceRepository
            val result = voiceRepository.recognizeVoice(File(processingRecord.audioFilePath))

            // 3. Успех - создаем новую копию с данными
            val successRecord = processingRecord.copy(
                recognizedAmount = result.amount,
                recognizedDescription = result.description,
                status = RecognitionStatus.SUCCESS
            )
            voiceRepository.saveAudioToQueue(successRecord)
        } catch (e: Exception) {
            // 4. Ошибка
            val errorRecord = processingRecord.copy(
                status = RecognitionStatus.FAILED,
                errorMessage = e.message
            )
            voiceRepository.saveAudioToQueue(errorRecord)
        }
    }
}