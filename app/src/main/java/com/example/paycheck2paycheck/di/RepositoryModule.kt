package com.example.paycheck2paycheck.di

import com.example.paycheck2paycheck.data.repository.BudgetRepositoryImpl
import com.example.paycheck2paycheck.data.repository.ExpenseRepositoryImpl
import com.example.paycheck2paycheck.data.repository.ScheduledPaymentRepositoryImpl
import com.example.paycheck2paycheck.data.repository.StreakRepositoryImpl
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ExpenseRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import com.example.paycheck2paycheck.domain.repository.StreakRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        impl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        impl: ExpenseRepositoryImpl
    ): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindScheduledPaymentRepository(
        impl: ScheduledPaymentRepositoryImpl
    ): ScheduledPaymentRepository

    @Binds
    @Singleton
    abstract fun bindStreakRepository(
        impl: StreakRepositoryImpl
    ): StreakRepository
}
