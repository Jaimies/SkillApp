package com.maxpoliakov.skillapp.data.extensions

import com.maxpoliakov.skillapp.data.file_system.GenericFile
import com.maxpoliakov.skillapp.domain.model.Backup

fun GenericFile.toBackup() = Backup(this.uri, this.createdAt)
