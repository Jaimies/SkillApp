package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.billing.BillingRepository
import com.maxpoliakov.skillapp.billing.BillingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideBillingRepository(billingRepository: BillingRepositoryImpl): BillingRepository
}
