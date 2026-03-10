package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.data.remote.dto.VoiceRecognitionResponse
import com.example.paycheck2paycheck.domain.model.VoiceRecording
import java.io.File

interface VoiceRepository {
    suspend fun recognizeVoice(audioFile: File): VoiceRecognitionResponse  // ← ДОБАВЬ
    suspend fun saveAudioToQueue(recording: VoiceRecording)
    suspend fun getQueue(): List<VoiceRecording>
    suspend fun removeFromQueue(id: String)
}