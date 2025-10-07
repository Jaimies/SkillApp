package com.theskillapp.skillapp.shared.permissions

interface PermissionRequester {
    fun requestNotificationPermissionIfNotGranted()
}
