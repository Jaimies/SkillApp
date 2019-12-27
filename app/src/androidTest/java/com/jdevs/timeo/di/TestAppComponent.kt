package com.jdevs.timeo.di

import com.jdevs.timeo.ui.activitydetail.ActivityDetailFragmentTest
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@Component(modules = [TestRepositoryModule::class])
interface TestAppComponent : AppComponent {

    @ExperimentalCoroutinesApi
    fun inject(test: ActivityDetailFragmentTest)
}