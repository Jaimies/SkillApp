package com.maxpoliakov.skillapp.ui.common.adapter

import com.maxpoliakov.skillapp.model.ViewItem
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

data class UiItem(override val id: Int, val param: Int) : ViewItem

class DiffCallbackTest : StringSpec({
    val callback = DiffCallback<UiItem>()

    "areItemsTheSame" {
        callback.areItemsTheSame(UiItem(1, 1), UiItem(1, 0)) shouldBe true
        callback.areItemsTheSame(UiItem(1, 0), UiItem(2, 0)) shouldBe false
    }

    "areContentsTheSame" {
        callback.areContentsTheSame(UiItem(1, 0), UiItem(1, 0)) shouldBe true
        callback.areContentsTheSame(UiItem(1, 0), UiItem(1, 1)) shouldBe false
    }
})
