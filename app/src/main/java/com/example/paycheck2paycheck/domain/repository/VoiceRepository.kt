package com.example.paycheck2paycheck.domain.repository

import com.example.paycheck2paycheck.domain.model.VoiceRecording

interface VoiceRepository {

    suspend fun saveAudioToQueue(recording: VoiceRecording)

    suspend fun getQueue(): List<VoiceRecording>

    suspend fun removeFromQueue(id: String)

}