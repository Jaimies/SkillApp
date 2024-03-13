package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.auth.GoogleAuthRepository
import com.maxpoliakov.skillapp.data.backup.BackupRepositoryImpl
import com.maxpoliakov.skillapp.data.group.DBSkillGroupRepository
import com.maxpoliakov.skillapp.data.preference.SharedPreferencesUserPreferenceRepository
import com.maxpoliakov.skillapp.data.records.DBRecordsRepository
import com.maxpoliakov.skillapp.data.skill.DBSkillRepository
import com.maxpoliakov.skillapp.data.stats.DBStatsRepository
import com.maxpoliakov.skillapp.data.timer.DBTimerRepository
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.domain.repository.TimerRepository
import com.maxpoliakov.skillapp.domain.repository.UserPreferenceRepository
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
    fun provideStatsRepository(repository: DBStatsRepository): StatsRepository

    @Binds
    fun provideSkillGroupRepository(repository: DBSkillGroupRepository): SkillGroupRepository

    @Binds
    fun provideAuthRepository(authRepository: GoogleAuthRepository): AuthRepository

    @Binds
    fun provideDriveRepository(repository: BackupRepositoryImpl): BackupRepository

    @Binds
    fun provideTimerRepository(repository: DBTimerRepository): TimerRepository

    @Binds
    fun provideUserPreferenceRepository(repository: SharedPreferencesUserPreferenceRepository): UserPreferenceRepository
}
