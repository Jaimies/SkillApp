package com.theskillapp.skillapp.domain.usecase.skill

import com.theskillapp.skillapp.domain.model.Orderable

interface UpdateOrderUseCase {
    suspend fun run(items: List<Orderable>)
}
