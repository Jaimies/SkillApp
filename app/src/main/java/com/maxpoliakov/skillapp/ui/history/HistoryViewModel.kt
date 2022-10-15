package com.maxpoliakov.skillapp.ui.history

import com.maxpoliakov.skillapp.domain.model.SelectionCriteria
import com.maxpoliakov.skillapp.domain.usecase.records.GetHistoryUseCase
import com.maxpoliakov.skillapp.ui.common.history.ViewModelWithHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getHistory: GetHistoryUseCase,
) : ViewModelWithHistory(getHistory) {
    override val selectionCriteria = SelectionCriteria.All
}
