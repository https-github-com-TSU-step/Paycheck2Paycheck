package com.example.paycheck2paycheck.data.di

import com.example.paycheck2paycheck.data.remote.MockVoiceRecognitionApi
import com.example.paycheck2paycheck.data.remote.VoiceRecognitionApi
import com.example.paycheck2paycheck.data.repository.VoiceRecognitionApiImpl
import com.example.paycheck2paycheck.data.repository.VoiceRepositoryImpl
import com.example.paycheck2paycheck.domain.repository.VoiceApi
import com.example.paycheck2paycheck.domain.repository.VoiceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VoiceModule {

    // Связываем интерфейс API (Data) с моком
    @Binds
    @Singleton
    abstract fun bindVoiceApi(
        impl: MockVoiceRecognitionApi
    ): VoiceRecognitionApi

    // Связываем доменный интерфейс с нашей реализацией-переводчиком
    @Binds
    @Singleton
    abstract fun bindVoiceService(
        impl: VoiceRecognitionApiImpl
    ): VoiceApi // Тот самый интерфейс из Domain

    @Binds
    @Singleton
    abstract fun bindVoiceRepository(
        impl: VoiceRepositoryImpl
    ): VoiceRepository
}