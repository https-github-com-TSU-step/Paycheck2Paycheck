package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.PendingAudioDao
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.data.remote.VoiceRecognitionApi
import com.example.paycheck2paycheck.domain.model.VoiceRecording
import com.example.paycheck2paycheck.domain.repository.VoiceRepository
import com.example.paycheck2paycheck.data.remote.dto.VoiceRecognitionResponse
import java.io.File
import javax.inject.Inject

class VoiceRepositoryImpl @Inject constructor(
    private val pendingAudioDao: PendingAudioDao,
    private val voiceApi: VoiceRecognitionApi  // ← ДОБАВЬ
) : VoiceRepository {

    override suspend fun recognizeVoice(audioFile: File): VoiceRecognitionResponse {
        // Вызов API для распознавания
        return voiceApi.recognizeAudio(audioFile)
    }

    override suspend fun saveAudioToQueue(recording: VoiceRecording) {
        pendingAudioDao.insert(recording.toEntity())
    }

    override suspend fun getQueue(): List<VoiceRecording> {
        return pendingAudioDao
            .getAllPending()
            .map { it.toDomain() }
    }

    override suspend fun removeFromQueue(id: String) {
        pendingAudioDao.deleteById(id)
    }
}