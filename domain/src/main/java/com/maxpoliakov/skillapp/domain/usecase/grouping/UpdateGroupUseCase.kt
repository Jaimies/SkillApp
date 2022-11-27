package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.Goal

interface UpdateGroupUseCase {
    suspend fun update(groupId: Int, newName: String, newGoal: Goal?)
}
