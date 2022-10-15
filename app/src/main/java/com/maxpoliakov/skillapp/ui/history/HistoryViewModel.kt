package com.maxpoliakov.skillapp.ui.history

import com.maxpoliakov.skillapp.domain.usecase.records.GetRecordsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetTimeAtDateUseCase
import com.maxpoliakov.skillapp.ui.common.history.ViewModelWithHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getRecords: GetRecordsUseCase,
    private val getTimeAtDate: GetTimeAtDateUseCase,
) : ViewModelWithHistory() {
    override val recordPagingData = getRecords.run()

    override suspend fun getTimeAtDate(date: LocalDate) = getTimeAtDate.run(date)
}
