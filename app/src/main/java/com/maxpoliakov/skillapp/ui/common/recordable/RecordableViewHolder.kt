package com.maxpoliakov.skillapp.ui.common.recordable

import android.view.View
import com.maxpoliakov.skillapp.model.Recordable
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

class RecordableViewHolder<T : Recordable>(
    view: View,
    private val viewModel: RecordableViewModel<*, T>,
    navigateToDetail: (index: Int) -> Unit
) : BaseViewHolder(view) {

    init {
        viewModel.navigateToDetails.observe { navigateToDetail(adapterPosition) }
    }

    fun setItem(item: T) = viewModel.setItem(item)
}
