package com.theskillapp.skillapp.domain.usecase.records

import com.theskillapp.skillapp.domain.model.Change
import com.theskillapp.skillapp.domain.model.Id
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EditRecordUseCaseImpl @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val deleteRecord: DeleteRecordUseCase,
    private val addRecord: AddRecordUseCase,
) : EditRecordUseCase {

    override suspend fun change(recordId: Id, change: Change<Record>) {
        val oldRecord = recordsRepository.getRecord(recordId).first() ?: return

        deleteRecord.run(recordId)
        addRecord.run(change.apply(oldRecord))
    }
}
