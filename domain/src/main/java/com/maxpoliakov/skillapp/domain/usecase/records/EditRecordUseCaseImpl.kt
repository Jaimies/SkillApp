package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Change
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import javax.inject.Inject

class EditRecordUseCaseImpl @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val deleteRecord: DeleteRecordUseCase,
    private val addRecord: AddRecordUseCase,
) : EditRecordUseCase {

    override suspend fun change(recordId: Id, change: Change<Record>) {
        val oldRecord = recordsRepository.getRecord(recordId) ?: return

        deleteRecord.run(recordId)
        addRecord.run(change.apply(oldRecord))
    }
}
