package com.theskillapp.skillapp.domain.usecase.records

import com.theskillapp.skillapp.domain.model.Change
import com.theskillapp.skillapp.domain.model.Id
import com.theskillapp.skillapp.domain.model.Record

interface EditRecordUseCase {
    suspend fun change(recordId: Id, change: Change<Record>)
}
