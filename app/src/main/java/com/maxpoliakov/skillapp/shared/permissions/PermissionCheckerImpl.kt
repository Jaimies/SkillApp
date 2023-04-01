package com.maxpoliakov.skillapp.shared.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionCheckerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
): PermissionChecker {
    override fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return isNotificationPermissionGranted()
        } else {
            return true
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    }
}
