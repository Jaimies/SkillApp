package com.jdevs.timeo.di

import android.content.Context
import com.jdevs.timeo.ui.activities.ActivitiesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: ActivitiesFragment)
}