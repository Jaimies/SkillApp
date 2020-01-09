package com.jdevs.timeo.model

import com.jdevs.timeo.common.adapter.ViewItem
import org.threeten.bp.OffsetDateTime

interface DataItem : ViewItem {

    val creationDate: OffsetDateTime
}
