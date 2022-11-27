package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.grouping.AddOrRemoveSkillToGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.AddOrRemoveSkillToGroupUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.grouping.DeleteGroupIfEmptyUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.DeleteGroupIfEmptyUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordDateUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordDateUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordTimeUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordTimeUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.records.DeleteRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.DeleteRecordUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.records.GetHistoryUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.GetHistoryUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.skill.ManageSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.ManageSkillUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateOrderUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateOrderUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentCountUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentCountUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Binds
    fun provideGetRecentCountUseCase(useCase: GetRecentCountUseCaseImpl): GetRecentCountUseCase

    @Binds
    fun provideGetStatsUseCase(useCase: GetStatsUseCaseImpl): GetStatsUseCase

    @Binds
    fun provideCreateBackupUseCase(useCase: CreateBackupUseCaseImpl): CreateBackupUseCase

    @Binds
    fun provideRestoreBackupUseCase(useCase: RestoreBackupUseCaseImpl): RestoreBackupUseCase

    @Binds
    fun provideGetGroupUseCase(useCase: GetGroupUseCaseImpl): GetGroupUseCase

    @Binds
    fun provideUpdateGroupUseCase(useCase: UpdateGroupUseCaseImpl): UpdateGroupUseCase

    @Binds
    fun provideDeleteGroupIfEmptyUseCase(useCase: DeleteGroupIfEmptyUseCaseImpl): DeleteGroupIfEmptyUseCase

    @Binds
    fun provideAddOrRemoveSkillToGroupUseCase(useCase: AddOrRemoveSkillToGroupUseCaseImpl): AddOrRemoveSkillToGroupUseCase

    @Binds
    fun provideManageSkillUseCase(useCase: ManageSkillUseCaseImpl): ManageSkillUseCase

    @Binds
    fun provideGetSkillsAndSkillGroupsUseCase(useCase: GetSkillsAndSkillGroupsUseCaseImpl): GetSkillsAndSkillGroupsUseCase

    @Binds
    fun provideGetSkillByIdUseCase(useCase: GetSkillByIdUseCaseImpl): GetSkillByIdUseCase

    @Binds
    fun provideUpdateOrderUseCase(useCase: UpdateOrderUseCaseImpl): UpdateOrderUseCase

    @Binds
    fun provideChangeRecordDateUseCase(useCase: ChangeRecordDateUseCaseImpl): ChangeRecordDateUseCase

    @Binds
    fun provideDeleteRecordUseCase(useCase: DeleteRecordUseCaseImpl): DeleteRecordUseCase

    @Binds
    fun provideChangeRecordTimeUseCase(useCase: ChangeRecordTimeUseCaseImpl): ChangeRecordTimeUseCase

    @Binds
    fun provideGetHistoryUseCase(useCase: GetHistoryUseCaseImpl): GetHistoryUseCase

    @Binds
    fun provideAddRecordUseCase(useCase: AddRecordUseCaseImpl): AddRecordUseCase
}
