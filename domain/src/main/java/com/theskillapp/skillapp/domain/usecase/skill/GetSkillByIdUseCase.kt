package com.theskillapp.skillapp.domain.usecase.skill

import com.theskillapp.skillapp.domain.model.Id
import com.theskillapp.skillapp.domain.model.Skill
import kotlinx.coroutines.flow.Flow

interface GetSkillByIdUseCase {
    fun run(id: Id): Flow<Skill>
}
