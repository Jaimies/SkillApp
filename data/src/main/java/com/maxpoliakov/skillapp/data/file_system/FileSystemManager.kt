package com.maxpoliakov.skillapp.data.file_system

import com.maxpoliakov.skillapp.domain.model.GenericUri

interface FileSystemManager {
    fun getChildren(uri: GenericUri): List<GenericFile>
    fun createFile(parentUri: GenericUri, name: String, mimeType: String, contents: String): GenericFile

    fun readFile(uri: GenericUri): String
}
