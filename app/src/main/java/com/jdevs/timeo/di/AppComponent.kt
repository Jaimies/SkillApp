package com.jdevs.timeo.di

import android.content.Context
import com.jdevs.timeo.data.di.ActivitiesModule
import com.jdevs.timeo.data.di.AuthModule
import com.jdevs.timeo.data.di.DatabaseModule
import com.jdevs.timeo.data.di.ProjectsModule
import com.jdevs.timeo.data.di.RecordsModule
import com.jdevs.timeo.data.di.SettingsModule
import com.jdevs.timeo.data.di.StatsModule
import com.jdevs.timeo.data.di.TasksModule
import com.jdevs.timeo.ui.activities.ActivitiesFragment
import com.jdevs.timeo.ui.activitydetail.ActivityDetailFragment
import com.jdevs.timeo.ui.addactivity.AddEditActivityFragment
import com.jdevs.timeo.ui.addproject.AddEditProjectFragment
import com.jdevs.timeo.ui.history.HistoryFragment
import com.jdevs.timeo.ui.overview.OverviewFragment
import com.jdevs.timeo.ui.profile.ProfileFragment
import com.jdevs.timeo.ui.projectdetail.ProjectDetailFragment
import com.jdevs.timeo.ui.projects.ProjectsFragment
import com.jdevs.timeo.ui.settings.SettingsFragment
import com.jdevs.timeo.ui.signin.SignInFragment
import com.jdevs.timeo.ui.signin.SignUpFragment
import com.jdevs.timeo.ui.tasks.AddTaskFragment
import com.jdevs.timeo.ui.tasks.TasksFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivitiesModule::class, RecordsModule::class, ProjectsModule::class, TasksModule::class,
        StatsModule::class, DatabaseModule::class, AuthModule::class, SettingsModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: OverviewFragment)
    fun inject(fragment: ActivitiesFragment)
    fun inject(fragment: ProjectsFragment)
    fun inject(fragment: AddEditActivityFragment)
    fun inject(fragment: AddEditProjectFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: ActivityDetailFragment)
    fun inject(fragment: ProjectDetailFragment)
    fun inject(fragment: AddTaskFragment)
    fun inject(fragment: TasksFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: SettingsFragment)
}
