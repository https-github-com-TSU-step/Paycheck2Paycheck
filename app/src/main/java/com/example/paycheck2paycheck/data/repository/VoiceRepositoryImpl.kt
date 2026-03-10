package com.example.paycheck2paycheck.data.repository

import com.example.paycheck2paycheck.data.local.dao.PendingAudioDao
import com.example.paycheck2paycheck.data.mapper.toDomain
import com.example.paycheck2paycheck.data.mapper.toEntity
import com.example.paycheck2paycheck.domain.model.VoiceRecording
import com.example.paycheck2paycheck.domain.repository.VoiceRepository
import javax.inject.Inject

class VoiceRepositoryImpl @Inject constructor(
    private val pendingAudioDao: PendingAudioDao
) : VoiceRepository {

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