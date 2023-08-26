package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.data.di.RepositoryModule
import com.maxpoliakov.skillapp.data.group.DBSkillGroupRepository
import com.maxpoliakov.skillapp.data.preference.SharedPreferencesUserPreferenceRepository
import com.maxpoliakov.skillapp.data.records.DBRecordsRepository
import com.maxpoliakov.skillapp.data.skill.DBSkillRepository
import com.maxpoliakov.skillapp.data.stats.DBStatsRepository
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.domain.repository.UserPreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class],
)
interface TestRepositoryModule {
    @Binds
    fun provideSkillRepository(repository: DBSkillRepository): SkillRepository

    @Binds
    fun provideRecordsRepository(repository: DBRecordsRepository): RecordsRepository

    @Binds
    fun provideSkillStatsRepository(repository: DBStatsRepository): StatsRepository

    @Binds
    fun provideSkillGroupRepository(repository: DBSkillGroupRepository): SkillGroupRepository

    @Binds
    fun provideAuthRepository(authRepository: StubAuthRepository): AuthRepository

    @Binds
    fun provideDriveRepository(repository: StubBackupRepository): BackupRepository

    @Binds
    fun provideUserPreferenceRepository(repository: SharedPreferencesUserPreferenceRepository): UserPreferenceRepository
}
