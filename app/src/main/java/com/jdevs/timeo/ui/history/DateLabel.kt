package com.jdevs.timeo.ui.history

import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.shared.util.shortName
import org.threeten.bp.LocalDate

data class DateLabel(val date: LocalDate) : ViewItem {
    val dateString = "${date.dayOfMonth} ${date.month.shortName}"
    override val id get() = dateString
}
