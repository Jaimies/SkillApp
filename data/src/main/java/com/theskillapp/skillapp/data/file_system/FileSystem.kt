package com.theskillapp.skillapp.data.file_system

import com.theskillapp.skillapp.domain.model.GenericUri

interface FileSystem {
    fun getChildren(uri: GenericUri): List<GenericFile>
    fun getLastChild(uri: GenericUri): GenericFile?

    fun createFile(parentUri: GenericUri, name: String, mimeType: String, contents: String): GenericFile

    fun readFile(uri: GenericUri): String
}
