package com.maxpoliakov.skillapp.data.file_system

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.maxpoliakov.skillapp.data.extensions.toByteArrayContent
import com.maxpoliakov.skillapp.data.extensions.toGenericFile
import com.maxpoliakov.skillapp.domain.model.GenericUri
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class GoogleDriveFileSystem @Inject constructor(
    private val drive: Drive,
): FileSystem {

    override fun getChildren(uri: GenericUri): List<GenericFile> {
        return getChildrenOrderedByModifiedTimeDesc(numberOfChildrenToFetch)
    }

    private fun getChildrenOrderedByModifiedTimeDesc(pageSize: Int): List<GenericFile> {
        return drive.files().list()
            .setPageSize(pageSize)
            .setOrderBy("modifiedTime desc")
            .setSpaces("appDataFolder")
            .setFields("files(id, createdTime)")
            .execute()
            .files
            .map(File::toGenericFile)
    }

    override fun getLastChild(uri: GenericUri): GenericFile? {
        // attempting to fetch just one last file might yield an empty list,
        // thus we fetch two
        return getChildrenOrderedByModifiedTimeDesc(2).firstOrNull()
    }

    override fun createFile(parentUri: GenericUri, name: String, mimeType: String, contents: String): GenericFile {
        return drive
            .files()
            .create(createDriveFile(parentUri, mimeType), contents.toByteArrayContent(mimeType))
            .execute()
            .toGenericFile()
    }

    private fun createDriveFile(parentUri: GenericUri, mimeType: String): File {
        return File()
            .setParents(listOf(parentUri.toString()))
            .setMimeType(mimeType)
    }

    override fun readFile(uri: GenericUri): String {
        return ByteArrayOutputStream()
            .also {
                drive.files().get(uri.uriString).executeMediaAndDownloadTo(it)
            }
            .toByteArray()
            .toString(StandardCharsets.UTF_8)
    }

    companion object {
        private const val numberOfChildrenToFetch = 30
    }
}
