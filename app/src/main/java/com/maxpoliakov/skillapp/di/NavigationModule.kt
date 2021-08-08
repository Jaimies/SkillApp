package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.util.navigation.NavigationUtil
import com.maxpoliakov.skillapp.util.navigation.NavigationUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface NavigationModule {
    @Binds
    fun provideNavigationUtil(navigationUtil: NavigationUtilImpl): NavigationUtil
}
