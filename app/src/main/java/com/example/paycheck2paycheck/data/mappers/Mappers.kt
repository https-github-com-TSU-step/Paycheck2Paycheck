package com.example.paycheck2paycheck.data.mapper

import androidx.compose.runtime.currentComposer
import com.example.paycheck2paycheck.data.local.entity.BudgetEntity
import com.example.paycheck2paycheck.data.local.entity.ExpenseEntity
import com.example.paycheck2paycheck.data.local.entity.PendingAudioEntity
import com.example.paycheck2paycheck.data.local.entity.ScheduledPaymentEntity
import com.example.paycheck2paycheck.data.local.entity.StreakEntity
import com.example.paycheck2paycheck.domain.model.Budget
import com.example.paycheck2paycheck.domain.model.Expense
import com.example.paycheck2paycheck.domain.model.RecognitionStatus
import com.example.paycheck2paycheck.domain.model.RecordingMethod
import com.example.paycheck2paycheck.domain.model.ScheduledPayment
import com.example.paycheck2paycheck.domain.model.Streak
import com.example.paycheck2paycheck.domain.model.VoiceRecording

// Expense Mapper
fun ExpenseEntity.toDomain(): Expense = Expense(
    id = id,
    name = description, // Entity.description -> Domain.name
    amount = amount,
    date = date,
    budgetId = budgetId,
    recordMethod = RecordingMethod.valueOf(recordingMethod), // Entity.recordingMethod -> Domain.recordMethod
    createdAt = createdAt,
    voiceRecordingId = null // В текущей ExpenseEntity этого поля нет, ставим null
)

fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
    id = id,
    budgetId = budgetId,
    amount = amount,
    description = name, // Domain.name -> Entity.description
    date = date,
    recordingMethod = recordMethod.name, // Domain.recordMethod -> Entity.recordingMethod
    createdAt = createdAt
)

// ScheduledPayment Mapper
fun ScheduledPaymentEntity.toDomain(): ScheduledPayment = ScheduledPayment(
    id = id,
    budgetId = budgetId,
    name = name,
    amount = amount,
    date = date,
    isPaid = isPaid
)

fun ScheduledPayment.toEntity(): ScheduledPaymentEntity = ScheduledPaymentEntity(
    id = id,
    budgetId = budgetId,
    amount = amount,
    name = name, // В Domain модели это name
    date = date,
    isPaid = isPaid
)

// Budget Mapper
fun BudgetEntity.toDomain(streak: Streak): Budget =
    Budget(
        id = id,
        totalAmount = totalAmount,
        startDate = startDate,
        endDate = endDate,
        dailyLimit = dailyLimit,
        remainingAmount = remainingAmount,
        createdAt = createdAt,
        updatedAt = updatedAt,
        curDayBudget = curDayBudget
    )
fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    totalAmount = totalAmount,
    startDate = startDate,
    endDate = endDate,
    dailyLimit = dailyLimit,
    remainingAmount = remainingAmount,
    createdAt = createdAt,
    updatedAt = updatedAt,
    curDayBudget = curDayBudget
)

fun StreakEntity.toDomain(): Streak = Streak(
    id = id,
    budgetId = budgetId,
    currentStreak = currentStreak,
    longestStreak = longestStreak,
    lastRecordedDate = lastRecordedDate,
    totalDaysTracked = totalDaysTracked
)

fun Streak.toEntity(): StreakEntity = StreakEntity(
    id = id,
    budgetId = budgetId,
    currentStreak = currentStreak,
    longestStreak = longestStreak,
    lastRecordedDate = lastRecordedDate,
    totalDaysTracked = totalDaysTracked
)

fun PendingAudioEntity.toDomain(): VoiceRecording = VoiceRecording(
    id = id,
    audioFilePath = audioFilePath,
    transcription = transcription,
    recognizedAmount = recognizedAmount,
    recognizedDescription = recognizedDescription,
    errorMessage = errorMessage,
    status = RecognitionStatus.valueOf(status),
    createdAt = createdAt
)

fun VoiceRecording.toEntity(): PendingAudioEntity = PendingAudioEntity(
    id = id,
    audioFilePath = audioFilePath,
    transcription = transcription,
    recognizedAmount = recognizedAmount,
    recognizedDescription = recognizedDescription,
    status = status.name,
    errorMessage = errorMessage,
    createdAt = createdAt
)