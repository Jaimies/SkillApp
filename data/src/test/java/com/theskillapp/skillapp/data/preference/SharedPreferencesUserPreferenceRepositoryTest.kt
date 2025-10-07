package com.theskillapp.skillapp.data.preference

import androidx.core.content.edit
import com.theskillapp.skillapp.data.StubSharedPreferences
import com.theskillapp.skillapp.domain.model.GenericUri
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import java.time.LocalTime

class SharedPreferencesUserPreferenceRepositoryTest : StringSpec({
    "getDayStartTime()" {
        val sharedPreferences = StubSharedPreferences(mapOf())
        val repository = SharedPreferencesUserPreferenceRepository(sharedPreferences)

        repository.getDayStartTime() shouldBe LocalTime.MIDNIGHT

        sharedPreferences.edit { putString("day_start_time", "12:34") }
        repository.getDayStartTime() shouldBe LocalTime.of(12, 34)

        sharedPreferences.edit { putString("day_start_time", "15:12") }
        repository.getDayStartTime() shouldBe LocalTime.of(15, 12)
    }

    "backupExportDirectory / setBackupExportDirectory()" {
        val sharedPreferences = StubSharedPreferences(mapOf())
        val repository = SharedPreferencesUserPreferenceRepository(sharedPreferences)

        repository.backupExportDirectory.first() shouldBe null

        repository.setBackupExportDirectory(GenericUri("directory"))
        repository.backupExportDirectory.first() shouldBe GenericUri("directory")

        repository.setBackupExportDirectory(null)
        repository.backupExportDirectory.first() shouldBe null
    }
})
