package com.maxpoliakov.skillapp.shared.permissions

interface PermissionRequester {
    fun requestNotificationPermissionIfNotGranted()
}
