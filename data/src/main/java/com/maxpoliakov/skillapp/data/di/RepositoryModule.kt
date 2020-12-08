package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.config.RemoteConfigRepositoryImpl
import com.maxpoliakov.skillapp.data.skill.SkillRepositoryImpl
import com.maxpoliakov.skillapp.data.records.RecordsRepositoryImpl
import com.maxpoliakov.skillapp.data.stats.StatsRepositoryImpl
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.RemoteConfigRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface RepositoryModule {
    @Binds
    fun provideSkillRepository(repository: SkillRepositoryImpl): SkillRepository

    @Binds
    fun provideRecordsRepository(repository: RecordsRepositoryImpl): RecordsRepository

    @Binds
    fun provideStatsRepository(repository: StatsRepositoryImpl): StatsRepository

    @Binds
    fun provideRemoteConfigRepository(repository: RemoteConfigRepositoryImpl): RemoteConfigRepository
}
