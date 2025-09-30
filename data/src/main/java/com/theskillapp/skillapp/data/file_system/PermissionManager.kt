package com.theskillapp.skillapp.data.file_system

import com.theskillapp.skillapp.domain.model.GenericUri

interface PermissionManager {
    fun persistReadAndWritePermissions(uri: GenericUri)
    fun releaseAllReadAndWritePermissions()
}
