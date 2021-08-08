package com.maxpoliakov.skillapp.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.maxpoliakov.skillapp.util.ui.findNavHostFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class NavControllerModule {
    @Provides
    fun provideNavController(activity: Activity): NavController {
        activity as AppCompatActivity
        return activity.findNavHostFragment().navController
    }
}
