package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.data.di.RepositoryModule
import com.maxpoliakov.skillapp.data.group.DBSkillGroupRepository
import com.maxpoliakov.skillapp.data.records.RecordsRepositoryImpl
import com.maxpoliakov.skillapp.data.skill.DBSkillRepository
import com.maxpoliakov.skillapp.data.stats.DBGroupStatsRepository
import com.maxpoliakov.skillapp.data.stats.DBSkillStatsRepository
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.GroupStatsRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
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
    fun provideRecordsRepository(repository: RecordsRepositoryImpl): RecordsRepository

    @Binds
    fun provideSkillStatsRepository(repository: DBSkillStatsRepository): SkillStatsRepository

    @Binds
    fun provideGroupStatsRepository(repository: DBGroupStatsRepository): GroupStatsRepository

    @Binds
    fun provideSkillGroupRepository(repository: DBSkillGroupRepository): SkillGroupRepository

    @Binds
    fun provideAuthRepository(authRepository: StubAuthRepository): AuthRepository

    @Binds
    fun provideDriveRepository(repository: StubBackupRepository): BackupRepository
}
