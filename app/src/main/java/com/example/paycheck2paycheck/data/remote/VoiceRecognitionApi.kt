package com.example.paycheck2paycheck.data.remote

import com.example.paycheck2paycheck.data.remote.dto.VoiceRecognitionResponse
import java.io.File

interface VoiceRecognitionApi {
    suspend fun recognizeAudio(audioFile: File): VoiceRecognitionResponse
}