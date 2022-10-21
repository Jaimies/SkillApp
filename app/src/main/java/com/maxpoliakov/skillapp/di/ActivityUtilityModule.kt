package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.ui.intro.IntroUtil
import com.maxpoliakov.skillapp.ui.intro.IntroUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface ActivityUtilityModule {
    @Binds
    fun provideIntroUtil(introUtil: IntroUtilImpl): IntroUtil
}
