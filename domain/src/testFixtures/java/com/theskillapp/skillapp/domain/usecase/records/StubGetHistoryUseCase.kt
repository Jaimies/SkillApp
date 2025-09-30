package com.theskillapp.skillapp.domain.usecase.records

import androidx.paging.PagingData
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class StubGetHistoryUseCase(
    private val records: List<Record>,
): GetHistoryUseCase {
    override fun getRecords(criteria: SkillSelectionCriteria): Flow<PagingData<Record>> {
        return flowOf(PagingData.from(records))
    }
}
