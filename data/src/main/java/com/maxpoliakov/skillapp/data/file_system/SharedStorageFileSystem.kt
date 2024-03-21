package com.maxpoliakov.skillapp.data.file_system

import android.content.ContentResolver
import android.content.Context
import androidx.documentfile.provider.DocumentFile
import com.maxpoliakov.skillapp.data.extensions.toAndroidUri
import com.maxpoliakov.skillapp.data.extensions.toGenericFile
import com.maxpoliakov.skillapp.domain.model.GenericUri
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
            .listFiles()
            .map(DocumentFile::toGenericFile)
            .toList()
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
}
