package com.theskillapp.skillapp.model

import com.theskillapp.skillapp.model.UiGoal.Type
import com.theskillapp.skillapp.model.UiMeasurementUnit.Millis
import com.theskillapp.skillapp.model.UiStatisticInterval.Daily
import com.theskillapp.skillapp.model.UiStatisticInterval.Weekly
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class BarChartDataTest : FunSpec({
    context("shouldDisplayGoal") {
        withData(
            BarChartData(Daily, listOf(), Millis, null) to false,
            BarChartData(Daily, listOf(), Millis, UiGoal(10, Type.Weekly, Millis)) to false,
            BarChartData(Weekly, listOf(), Millis, UiGoal(10, Type.Daily, Millis)) to false,
            BarChartData(Daily, listOf(), Millis, UiGoal(10, Type.Daily, Millis)) to true,
            BarChartData(Weekly, listOf(), Millis, UiGoal(10, Type.Weekly, Millis)) to true,
        ) { (chartData, shouldDisplay) ->
            chartData.shouldDisplayGoal shouldBe shouldDisplay
        }
    }
})
