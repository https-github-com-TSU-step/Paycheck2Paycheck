package com.example.paycheck2paycheck.di

import com.example.paycheck2paycheck.data.remote.MockVoiceRecognitionApi
import com.example.paycheck2paycheck.data.remote.VoiceRecognitionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideVoiceRecognitionApi(): VoiceRecognitionApi {
        return MockVoiceRecognitionApi()
    }
}