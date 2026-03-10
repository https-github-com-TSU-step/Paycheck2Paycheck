package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.remote.MockVoiceRecognitionApi
import com.example.paycheck2paycheck.domain.repository.VoiceApi
import com.example.paycheck2paycheck.domain.repository.VoiceResult
import java.io.File
import javax.inject.Inject

class VoiceRecognitionApiImpl @Inject constructor(
    private val mockApi: MockVoiceRecognitionApi // Используем твой существующий мок
) : VoiceApi {

    override suspend fun recognize(audioPath: String): VoiceResult {
        // Конвертируем путь в File (деталь платформы)
        val file = File(audioPath)

        // Вызываем твой мок
        val response = mockApi.recognizeAudio(file)

        // Конвертируем ответ мока (DTO) в чистые данные для Домена
        return VoiceResult(
            amount = response.amount,
            description = response.description
        )
    }
}