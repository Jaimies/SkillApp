package com.theskillapp.skillapp.data.di

import com.theskillapp.skillapp.data.backup.BackupRepositoryImpl
import com.theskillapp.skillapp.data.group.DBSkillGroupRepository
import com.theskillapp.skillapp.data.preference.SharedPreferencesUserPreferenceRepository
import com.theskillapp.skillapp.data.records.DBRecordsRepository
import com.theskillapp.skillapp.data.skill.DBSkillRepository
import com.theskillapp.skillapp.data.stats.DBStatsRepository
import com.theskillapp.skillapp.data.timer.DBTimerRepository
import com.theskillapp.skillapp.domain.repository.AuthRepository
import com.theskillapp.skillapp.domain.repository.BackupRepository
import com.theskillapp.skillapp.domain.repository.RecordsRepository
import com.theskillapp.skillapp.domain.repository.SkillGroupRepository
import com.theskillapp.skillapp.domain.repository.SkillRepository
import com.theskillapp.skillapp.domain.repository.StatsRepository
import com.theskillapp.skillapp.domain.repository.TimerRepository
import com.theskillapp.skillapp.domain.repository.UserPreferenceRepository
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
    fun provideTimerRepository(repository: DBTimerRepository): TimerRepository

    @Binds
    fun provideUserPreferenceRepository(repository: SharedPreferencesUserPreferenceRepository): UserPreferenceRepository
}
