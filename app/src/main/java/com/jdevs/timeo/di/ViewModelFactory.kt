package com.jdevs.timeo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdevs.timeo.ui.activities.ActivitiesViewModel
import com.jdevs.timeo.ui.activitydetail.ActivityDetailViewModel
import com.jdevs.timeo.ui.addactivity.AddEditActivityViewModel
import com.jdevs.timeo.ui.addproject.AddEditProjectViewModel
import com.jdevs.timeo.ui.history.HistoryViewModel
import com.jdevs.timeo.ui.overview.OverviewViewModel
import com.jdevs.timeo.ui.profile.ProfileViewModel
import com.jdevs.timeo.ui.projectdetail.ProjectDetailViewModel
import com.jdevs.timeo.ui.projects.ProjectsViewModel
import com.jdevs.timeo.ui.settings.SettingsViewModel
import com.jdevs.timeo.ui.signin.SignInViewModel
import com.jdevs.timeo.ui.signin.SignUpViewModel
import com.jdevs.timeo.ui.tasks.AddTaskViewModel
import com.jdevs.timeo.ui.tasks.TasksViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return viewModels[modelClass]?.get() as T
    }
}

@MapKey
@Retention
@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module(includes = [ProfileViewModelModule::class, OverviewViewModelModule::class])
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Module
interface OverviewViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    fun bindOverviewViewModel(viewModel: OverviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProjectsViewModel::class)
    fun bindProjectsViewModel(viewModel: ProjectsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProjectViewModel::class)
    fun bindAddProjectViewModel(viewModel: AddEditProjectViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProjectDetailViewModel::class)
    fun bindProjectDetailViewModel(viewModel: ProjectDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    fun bindAddTaskViewModel(viewModel: AddTaskViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    fun bindTasksViewModel(viewModel: TasksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivitiesViewModel::class)
    fun bindActivitiesViewModel(viewModel: ActivitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditActivityViewModel::class)
    fun bindAddActivityViewModel(viewModel: AddEditActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    fun bindHistoryViewModel(viewModel: HistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivityDetailViewModel::class)
    fun bindActivityDetailViewModel(viewModel: ActivityDetailViewModel): ViewModel
}

@Module
interface ProfileViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
