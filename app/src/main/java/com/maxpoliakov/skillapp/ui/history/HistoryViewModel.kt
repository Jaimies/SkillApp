package com.maxpoliakov.skillapp.ui.history

import com.maxpoliakov.skillapp.domain.usecase.records.GetRecordsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetTotalTimeAtDayUseCase
import com.maxpoliakov.skillapp.ui.common.history.ViewModelWithHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getRecords: GetRecordsUseCase,
    getTotalTimeAtDay: GetTotalTimeAtDayUseCase
) : ViewModelWithHistory(getTotalTimeAtDay) {
    override val recordPagingData = getRecords.run()
}
