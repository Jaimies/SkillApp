package com.maxpoliakov.skillapp.data.backup

import android.content.Context
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.file.Files
import javax.inject.Inject

class BackupUtilImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): BackupUtil {
    override fun getDatabaseBackup(): String {
        val dbFile = context.getDatabasePath(AppDatabase.DATABASE_NAME)
        return String(Files.readAllBytes(dbFile.toPath()))
    }
}
