package com.theskillapp.skillapp.di

import com.theskillapp.skillapp.domain.time.DateProvider
import com.theskillapp.skillapp.domain.time.DefaultDateProvider
import com.theskillapp.skillapp.shared.time.DateFormatter
import com.theskillapp.skillapp.shared.time.DefaultDateFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TimeModule {
    @Binds
    fun provideDateProvider(dateProvider: DefaultDateProvider): DateProvider

    @Binds
    fun provideDateFormatter(dateFormatter: DefaultDateFormatter): DateFormatter
}
