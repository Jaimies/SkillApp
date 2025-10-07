package com.theskillapp.skillapp.domain.usecase.grouping

interface DeleteGroupIfEmptyUseCase {
    suspend fun run(groupId: Int)
}
