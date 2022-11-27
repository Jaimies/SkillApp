package com.maxpoliakov.skillapp.domain.usecase.grouping

interface DeleteGroupIfEmptyUseCase {
    suspend fun run(groupId: Int)
}
