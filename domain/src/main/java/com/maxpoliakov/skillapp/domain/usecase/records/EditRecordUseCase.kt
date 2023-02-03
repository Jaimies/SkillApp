package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Change
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record

interface EditRecordUseCase {
    suspend fun change(recordId: Id, change: Change<Record>)
}
