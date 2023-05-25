package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.auth.GoogleAuthRepository
import com.maxpoliakov.skillapp.data.drive.GoogleDriveBackupRepository
import com.maxpoliakov.skillapp.data.group.DBSkillGroupRepository
import com.maxpoliakov.skillapp.data.records.DBRecordsRepository
import com.maxpoliakov.skillapp.data.skill.DBSkillRepository
import com.maxpoliakov.skillapp.data.stats.DBGroupStatsRepository
import com.maxpoliakov.skillapp.data.stats.DBSkillStatsRepository
import com.maxpoliakov.skillapp.data.timer.DBTimerRepository
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.GroupStatsRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.domain.repository.TimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideSkillRepository(repository: DBSkillRepository): SkillRepository

    @Binds
    fun provideRecordsRepository(repository: DBRecordsRepository): RecordsRepository

    @Binds
    fun provideSkillStatsRepository(repository: DBSkillStatsRepository): SkillStatsRepository

    @Binds
    fun provideGroupStatsRepository(repository: DBGroupStatsRepository): GroupStatsRepository

    @Binds
    fun provideSkillGroupRepository(repository: DBSkillGroupRepository): SkillGroupRepository

    @Binds
    fun provideAuthRepository(authRepository: GoogleAuthRepository): AuthRepository

    @Binds
    fun provideDriveRepository(repository: GoogleDriveBackupRepository): BackupRepository

    @Binds
    fun provideTimerRepository(repository: DBTimerRepository): TimerRepository
}
