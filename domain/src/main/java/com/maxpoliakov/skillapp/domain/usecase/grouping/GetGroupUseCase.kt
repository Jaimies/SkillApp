package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.SkillGroup
import kotlinx.coroutines.flow.Flow

interface GetGroupUseCase {
    fun getById(id: Int): Flow<SkillGroup>
}
