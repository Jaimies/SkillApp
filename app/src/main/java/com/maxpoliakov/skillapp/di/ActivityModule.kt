package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.ui.intro.IntroUtil
import com.maxpoliakov.skillapp.ui.intro.IntroUtilImpl
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.tracking.RecordUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface ActivityModule {
    @Binds
    fun provideIntroUtil(introUtil: IntroUtilImpl): IntroUtil

    @Binds
    fun provideRecordUtil(recordUtil: RecordUtilImpl): RecordUtil
}
