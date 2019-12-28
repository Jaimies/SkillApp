package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.FakeUserManager
import com.jdevs.timeo.data.source.UserManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestAuthModule {

    @Provides
    @Singleton
    fun provideUserManager(): UserManager = FakeUserManager

    @Provides
    @Singleton
    fun provideFakeUserManager(userManager: UserManager) = userManager as FakeUserManager
}
