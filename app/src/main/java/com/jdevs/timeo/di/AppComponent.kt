package com.jdevs.timeo.di

import android.content.Context
import com.jdevs.timeo.ui.activities.ActivitiesFragment
import com.jdevs.timeo.ui.addactivity.AddEditActivityFragment
import com.jdevs.timeo.ui.history.HistoryFragment
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
    fun inject(fragment: AddEditActivityFragment)
    fun inject(fragment: HistoryFragment)
}
