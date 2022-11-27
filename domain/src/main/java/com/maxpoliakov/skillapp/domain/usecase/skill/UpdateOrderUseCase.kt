package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Orderable

interface UpdateOrderUseCase {
    suspend fun run(items: List<Orderable>)
}
