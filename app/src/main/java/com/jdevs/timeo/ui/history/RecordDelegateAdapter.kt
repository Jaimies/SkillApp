package com.jdevs.timeo.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.RecordsItemBinding
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.util.createViewModel
import com.jdevs.timeo.util.fragmentActivity

class RecordDelegateAdapter(private val showDeleteDialog: (Int) -> Unit = {}) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.fragmentActivity
        val viewModel = createViewModel(fragmentActivity, RecordViewModel::class)

        val binding = RecordsItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = fragmentActivity
        }

        return ViewHolder(binding.root, viewModel, showDeleteDialog)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.bindRecord(item as RecordItem)
    }

    class ViewHolder(
        private val view: View,
        private val viewModel: RecordViewModel,
        private val showDeleteDialog: (Int) -> Unit
    ) : PagingAdapter.ViewHolder(view) {

        init {

            observeEvent(viewModel.showDeleteDialog) { showDeleteDialog(adapterPosition) }
        }

        fun bindRecord(record: RecordItem) {

            val backgroundColorId =
                if (adapterPosition.rem(2) == 0) R.color.colorBlackTransparent else android.R.color.transparent

            view.setBackgroundColor(ContextCompat.getColor(context, backgroundColorId))
            viewModel.setRecord(record)
        }
    }
}
