package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.data.di.RepositoryModule
import com.maxpoliakov.skillapp.data.group.SkillGroupRepositoryImpl
import com.maxpoliakov.skillapp.data.records.RecordsRepositoryImpl
import com.maxpoliakov.skillapp.data.skill.SkillRepositoryImpl
import com.maxpoliakov.skillapp.data.stats.StatsRepositoryImpl
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
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
    fun provideSkillRepository(repository: SkillRepositoryImpl): SkillRepository

    @Binds
    fun provideRecordsRepository(repository: RecordsRepositoryImpl): RecordsRepository

    @Binds
    fun provideStatsRepository(repository: StatsRepositoryImpl): StatsRepository

    @Binds
    fun provideSkillGroupRepository(repository: SkillGroupRepositoryImpl): SkillGroupRepository

    @Binds
    fun provideAuthRepository(authRepository: StubAuthRepository): AuthRepository

    @Binds
    fun provideDriveRepository(repository: StubDriveRepository): DriveRepository

    @Binds
    fun provideBillingRepository(billingRepository: StubBillingRepository): BillingRepository

    @Binds
    fun provideExtendedBillingRepository(billingRepository: StubBillingRepository): ExtendedBillingRepository
}
