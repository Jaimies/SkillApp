package com.maxpoliakov.skillapp.domain.usecase.records

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import kotlinx.coroutines.flow.Flow

interface GetHistoryUseCase {
    fun getRecords(criteria: SkillSelectionCriteria): Flow<PagingData<Record>>
}
