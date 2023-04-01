package com.maxpoliakov.skillapp.shared.permissions

interface PermissionChecker {
    fun hasNotificationPermission() : Boolean
}
