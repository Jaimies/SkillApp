package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Skill
import kotlinx.coroutines.flow.Flow

interface GetSkillByIdUseCase {
    fun run(id: Id): Flow<Skill>
}
