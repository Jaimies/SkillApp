package com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch

import com.maxpoliakov.skillapp.domain.model.Skill
import java.time.ZonedDateTime

data class StopwatchUiModel(
    val skill: Skill,
    val startTime: ZonedDateTime,
    val stop: () -> Unit,
    val navigateToDetail: () -> Unit,
)
