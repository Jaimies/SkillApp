package com.maxpoliakov.skillapp.domain.usecase.records

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetHistoryUseCase {
    fun getRecords(criteria: SkillSelectionCriteria): Flow<PagingData<Record>>

    fun getCount(criteria: SkillSelectionCriteria, range: ClosedRange<LocalDate>): Flow<Long>
    fun getCount(criteria: SkillSelectionCriteria, date: LocalDate): Flow<Long>
}
