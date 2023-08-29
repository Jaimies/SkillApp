package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.domain.time.DateProvider
import com.maxpoliakov.skillapp.domain.time.DefaultDateProvider
import com.maxpoliakov.skillapp.shared.time.DateFormatter
import com.maxpoliakov.skillapp.shared.time.DefaultDateFormatter
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
