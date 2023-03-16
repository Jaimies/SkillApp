package com.maxpoliakov.skillapp.ui.history

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.shared.history.ViewModelWithHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModelWithHistory() {
    override val selectionCriteria = SkillSelectionCriteria.Any
    override val unitForDailyTotals = flowOf(MeasurementUnit.Millis)
}
