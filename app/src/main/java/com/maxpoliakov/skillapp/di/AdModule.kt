package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.util.ads.AdProvider
import com.maxpoliakov.skillapp.util.ads.AdProviderImpl
import com.maxpoliakov.skillapp.util.ads.AdUtil
import com.maxpoliakov.skillapp.util.ads.AdUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface AdModule {
    @Binds
    fun bindAdUtil(adUtilImpl: AdUtilImpl): AdUtil

    @Binds
    fun bindAdProvider(adProviderImpl: AdProviderImpl): AdProvider
}
