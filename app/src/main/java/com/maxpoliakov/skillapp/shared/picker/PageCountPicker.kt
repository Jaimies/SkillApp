package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.Count
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

open class PageCountPicker : IntegerValuePicker<Count>(MeasurementUnit.Pages)
