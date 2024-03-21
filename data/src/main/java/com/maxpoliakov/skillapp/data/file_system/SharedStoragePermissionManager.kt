package com.maxpoliakov.skillapp.data.file_system

import android.content.ContentResolver
import android.content.Intent
import com.maxpoliakov.skillapp.data.extensions.toAndroidUri
import com.maxpoliakov.skillapp.domain.model.GenericUri
import javax.inject.Inject

class SharedStoragePermissionManager @Inject constructor(
    private val contentResolver: ContentResolver,
): PermissionManager {

    override fun persistReadAndWritePermissions(uri: GenericUri) {
        contentResolver.takePersistableUriPermission(uri.toAndroidUri(), readAndWritePermissionFlags)
    }

    override fun releaseAllReadAndWritePermissions() {
        contentResolver.persistedUriPermissions.map { permission ->
            contentResolver.releasePersistableUriPermission(permission.uri, readAndWritePermissionFlags)
        }
    }

    companion object {
        private const val readAndWritePermissionFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    }
}
