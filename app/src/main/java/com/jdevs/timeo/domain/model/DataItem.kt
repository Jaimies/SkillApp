package com.jdevs.timeo.domain.model

import com.jdevs.timeo.ui.common.adapter.ViewItem
import org.threeten.bp.OffsetDateTime

interface DataItem : ViewItem {

    val creationDate: OffsetDateTime
}
