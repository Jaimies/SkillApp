package com.maxpoliakov.skillapp.shared.util

import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.mockito.Mockito
import org.mockito.Mockito.times

class FlowTest : StringSpec({
    "collectOnce" {
        val collector = Mockito.mock({ _: Unit -> }::class.java)
        val flow = flow {
            while (isActive) {
                delay(1)
                emit(Unit)
            }
        }

        flow.collectOnce(collector)
        delay(5)

        Mockito.verify(collector, times(1)).invoke(Unit)
    }
})
