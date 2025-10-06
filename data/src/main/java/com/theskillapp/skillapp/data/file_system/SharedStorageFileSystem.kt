package com.theskillapp.skillapp.data.file_system

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Document.COLUMN_DOCUMENT_ID
import android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME
import android.provider.DocumentsContract.Document.COLUMN_LAST_MODIFIED
import androidx.documentfile.provider.DocumentFile
import com.theskillapp.skillapp.data.extensions.localDateTimeOfEpochMilli
import com.theskillapp.skillapp.data.extensions.toAndroidUri
import com.theskillapp.skillapp.data.extensions.toGenericFile
import com.theskillapp.skillapp.data.extensions.toGenericUri
import com.theskillapp.skillapp.domain.model.GenericUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.Reader
import javax.inject.Inject

class SharedStorageFileSystem @Inject constructor(
    private val contentResolver: ContentResolver,
    @ApplicationContext
    private val context: Context,
): FileSystem {

    override fun getChildren(uri: GenericUri): List<GenericFile> {
        return getDocumentTreeFile(uri)
            .listFilesOptimized()
            .sortedByDescending(GenericFile::lastModificationDate)
    }

    // Uses ContentResolver directly rather than via DocumentFile,
    // because DocumentFile::listFiles() is incredibly slow,
    // see https://stackoverflow.com/a/66296597/11105280
    // Filtering and sorting is done manually, because using selection, sortOrder
    // arguments of ContentResolver's query method seems to have no effect.
    private fun DocumentFile.listFilesOptimized(): List<GenericFile> {
        val parentDocumentId = DocumentsContract.getDocumentId(uri)
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, parentDocumentId)

        return contentResolver.query(
            childrenUri, arrayOf(
                COLUMN_DOCUMENT_ID,
                COLUMN_DISPLAY_NAME,
                COLUMN_LAST_MODIFIED,
            ), null, null, null
        )!!.use { cursor ->
            val result = ArrayList<GenericFile>(cursor.getCount())

            while (cursor.moveToNext()) {
                val name = cursor.getString(1)
                if (!name.startsWith("com.theskillapp.skillapp_backup_") || !name.endsWith(".json")) continue
                val documentId = cursor.getString(0)
                val uri = DocumentsContract.buildDocumentUriUsingTree(this.uri, documentId).toGenericUri()
                val lastModified = localDateTimeOfEpochMilli(cursor.getLong(2))

                result.add(GenericFile(uri, lastModified))
            }

            result
        }
    }

    override fun getLastChild(uri: GenericUri): GenericFile? {
        return getChildren(uri).maxByOrNull(GenericFile::lastModificationDate)
    }

    override fun createFile(parentUri: GenericUri, name: String, mimeType: String, contents: String): GenericFile {
        return getDocumentTreeFile(parentUri)
            .createFile(mimeType, name)!!
            .also { writeFile(it, contents) }
            .toGenericFile()
    }

    private fun writeFile(file: DocumentFile, contents: String) {
        contentResolver
            .openOutputStream(file.uri)!!
            .use { stream ->
                stream.write(contents.toByteArray())
            }
    }

    override fun readFile(uri: GenericUri): String {
        return contentResolver
            .openInputStream(uri.toAndroidUri())!!
            .use { stream ->
                stream.reader().use(Reader::readText)
            }
    }

    // null-asserting the value returned by DocumentFile::fromTreeUri() is safe
    // because the function will only return null if the api level is below 21
    // (or minimum is 23)
    private fun getDocumentTreeFile(uri: GenericUri) : DocumentFile {
        return DocumentFile.fromTreeUri(context, uri.toAndroidUri())!!
    }

    companion object {
        const val TAG = "SharedStorageFileSystem"
    }
}
