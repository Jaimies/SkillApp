package com.maxpoliakov.skillapp.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.shared.snackbar.SnackbarShower
import com.maxpoliakov.skillapp.shared.snackbar.SnackbarShowerImpl
import com.maxpoliakov.skillapp.shared.tracking.EditRecordTimeDialogShower
import com.maxpoliakov.skillapp.shared.tracking.EditRecordTimeDialogShowerImpl
import com.maxpoliakov.skillapp.shared.tracking.RecordAddedSnackbarLabelFormatter
import com.maxpoliakov.skillapp.shared.tracking.RecordAddedSnackbarLabelFormatterImpl
import com.maxpoliakov.skillapp.shared.tracking.RecordAddedSnackbarShower
import com.maxpoliakov.skillapp.shared.tracking.RecordAddedSnackbarShowerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface ActivityModule {
    @Binds
    fun provideSnackbarShower(snackbarShower: SnackbarShowerImpl): SnackbarShower

    @Binds
    fun provideSnackbarLabelFormatter(snackbarLabelFormatter: RecordAddedSnackbarLabelFormatterImpl): RecordAddedSnackbarLabelFormatter

    @Binds
    fun provideRecordAddedSnackbarShower(recordAddedSnackbarShower: RecordAddedSnackbarShowerImpl): RecordAddedSnackbarShower

    @Binds
    fun provideEditRecordTimeDialogShower(editRecordTimeDialogShower: EditRecordTimeDialogShowerImpl): EditRecordTimeDialogShower

    companion object {
        @Provides
        fun provideFragmentManager(activity: Activity): FragmentManager {
            return (activity as AppCompatActivity).supportFragmentManager
        }
    }
}
