package com.maxpoliakov.skillapp.shared.permissions

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PermissionRequesterImpl @Inject constructor(
    private val activity: Activity,
    private val permissionChecker: PermissionChecker,
) : PermissionRequester {

    override fun requestNotificationPermissionIfNotGranted() {
        if (!permissionChecker.hasNotificationPermission()) {
            requestNotificationPermissionIfApiLevelAtLeastTiramisu()
        }
    }

    private fun requestNotificationPermissionIfApiLevelAtLeastTiramisu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PERMISSION_REQUEST_CODE,
        )
    }

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 0
    }
}
