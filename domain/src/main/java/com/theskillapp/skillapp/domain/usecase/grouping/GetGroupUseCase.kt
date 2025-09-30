package com.theskillapp.skillapp.domain.usecase.grouping

import com.theskillapp.skillapp.domain.model.SkillGroup
import kotlinx.coroutines.flow.Flow

interface GetGroupUseCase {
    fun getById(id: Int): Flow<SkillGroup>
}
