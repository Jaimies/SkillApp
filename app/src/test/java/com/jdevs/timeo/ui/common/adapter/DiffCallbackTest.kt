package com.jdevs.timeo.ui.common.adapter

import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DiffCallback.areContentsTheSame
import com.jdevs.timeo.ui.common.adapter.DiffCallback.areItemsTheSame
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

data class UiItem(override val id: Int, val param: Int) : ViewItem

class DiffCallbackTest : StringSpec({
    "areItemsTheSame" {
        areItemsTheSame(UiItem(1, 1), UiItem(1, 0)) shouldBe true
        areItemsTheSame(UiItem(1, 0), UiItem(2, 0)) shouldBe false
    }

    "areContentsTheSame" {
        areContentsTheSame(UiItem(1, 0), UiItem(1, 0)) shouldBe true
        areContentsTheSame(UiItem(1, 0), UiItem(1, 1)) shouldBe false
    }
})
