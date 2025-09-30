package com.theskillapp.skillapp.domain.usecase.records

import androidx.paging.PagingData
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import kotlinx.coroutines.flow.Flow

interface GetHistoryUseCase {
    fun getRecords(criteria: SkillSelectionCriteria): Flow<PagingData<Record>>
}
