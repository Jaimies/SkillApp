package com.theskillapp.skillapp.domain.usecase.grouping

import com.theskillapp.skillapp.domain.model.Goal

interface UpdateGroupUseCase {
    suspend fun update(groupId: Int, newName: String, newGoal: Goal?)
}
