package com.example.paycheck2paycheck.domain.usecase

import com.example.paycheck2paycheck.domain.model.RecognitionStatus
import com.example.paycheck2paycheck.domain.model.VoiceRecording
import com.example.paycheck2paycheck.domain.repository.VoiceRepository
import java.io.File
import javax.inject.Inject

class ProcessVoiceRecognitionUseCase @Inject constructor(
    private val voiceRepository: VoiceRepository
) {
    suspend operator fun invoke(recording: VoiceRecording) {
        val processingRecord = recording.copy(
            status = RecognitionStatus.PROCESSING,
            errorMessage = null
        )
        voiceRepository.saveAudioToQueue(processingRecord)

        try {
            val result = voiceRepository.recognizeVoice(File(processingRecord.audioFilePath))

            val successRecord = processingRecord.copy(
                recognizedAmount = result.amount,
                recognizedDescription = result.description,
                status = RecognitionStatus.SUCCESS
            )
            voiceRepository.saveAudioToQueue(successRecord)
        } catch (e: Exception) {
            val errorRecord = processingRecord.copy(
                status = RecognitionStatus.FAILED,
                errorMessage = e.message
            )
            voiceRepository.saveAudioToQueue(errorRecord)
        }
    }
}