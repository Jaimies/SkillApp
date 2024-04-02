package com.maxpoliakov.skillapp.data.di

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BackupBackend: Parcelable {
    Local,
    GoogleDrive,
}
