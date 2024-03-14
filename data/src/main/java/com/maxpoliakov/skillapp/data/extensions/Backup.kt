package com.maxpoliakov.skillapp.data.extensions

import com.maxpoliakov.skillapp.data.file_system.GenericFile
import com.maxpoliakov.skillapp.domain.model.Backup

// since we never modify backup files after creating them, we can assume
// that lastModificationDate == creationDate
fun GenericFile.toBackup() = Backup(this.uri, this.lastModificationDate)
