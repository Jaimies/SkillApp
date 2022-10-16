package com.maxpoliakov.skillapp.ui.history

import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.ui.common.history.ViewModelWithHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModelWithHistory() {
    override val selectionCriteria = SkillSelectionCriteria.Any
}
