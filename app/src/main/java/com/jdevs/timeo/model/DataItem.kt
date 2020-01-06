package com.jdevs.timeo.model

import com.jdevs.timeo.common.adapter.ViewItem
import org.threeten.bp.OffsetDateTime

abstract class DataItem : ViewItem {

    abstract val creationDate: OffsetDateTime
}
