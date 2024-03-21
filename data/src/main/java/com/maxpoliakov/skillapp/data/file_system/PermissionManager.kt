package com.maxpoliakov.skillapp.data.file_system

import com.maxpoliakov.skillapp.domain.model.GenericUri

interface PermissionManager {
    fun persistReadAndWritePermissions(uri: GenericUri)
    fun releaseAllReadAndWritePermissions()
}
