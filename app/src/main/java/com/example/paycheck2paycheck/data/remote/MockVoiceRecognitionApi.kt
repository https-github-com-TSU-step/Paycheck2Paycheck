package com.example.paycheck2paycheck.data.remote

import com.example.paycheck2paycheck.data.remote.dto.VoiceRecognitionResponse
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.delay

class MockVoiceRecognitionApi @Inject constructor() : VoiceRecognitionApi {

    override suspend fun recognizeAudio(audioFile: File): VoiceRecognitionResponse {
        // Имитация сетевого запроса
        delay(1000)

        // Возвращаем захардкоженный результат
        return VoiceRecognitionResponse(
            amount = 500.0,
            description = "кофе",
            success = true
        )
    }
}