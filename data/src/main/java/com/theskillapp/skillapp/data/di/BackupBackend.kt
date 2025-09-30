package com.theskillapp.skillapp.data.di

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BackupBackend: Parcelable {
    Local,
}
