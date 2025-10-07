package com.theskillapp.skillapp.ui.history

import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.shared.history.ViewModelWithHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModelWithHistory() {
    override val selectionCriteria = SkillSelectionCriteria.Any
    override val unitForDailyTotals = flowOf(MeasurementUnit.Millis)
}
