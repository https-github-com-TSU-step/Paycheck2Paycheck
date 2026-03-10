package com.example.paycheck2paycheck.data.di

import android.content.Context
import androidx.room.Room
import com.example.paycheck2paycheck.data.local.AppDatabase
import com.example.paycheck2paycheck.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideBudgetDao(db: AppDatabase): BudgetDao = db.budgetDao()

    @Provides
    fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()

    @Provides
    fun provideScheduledPaymentDao(db: AppDatabase): ScheduledPaymentDao = db.scheduledPaymentDao()

    @Provides
    fun provideStreakDao(db: AppDatabase): StreakDao = db.streakDao()

    @Provides
    fun providePendingAudioDao(db: AppDatabase): PendingAudioDao = db.pendingAudioDao()
}