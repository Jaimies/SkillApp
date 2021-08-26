package com.maxpoliakov.skillapp.data.di

import android.accounts.Account
import android.content.Context
import android.content.SharedPreferences
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.maxpoliakov.skillapp.data.persistence.getStringPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DriveModule {
    @Provides
    fun provideDriveService(
        @ApplicationContext context: Context,
        prefs: SharedPreferences,
    ): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_APPDATA)).apply {
            val name = prefs.getStringPreference("account.name", "")
            val type = prefs.getStringPreference("account.type", "")

            if(name.isNotEmpty() && type.isNotEmpty())
            selectedAccount = Account(name, type)
        }

        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        )
            .setApplicationName("SkillApp")
            .build()
    }
}
