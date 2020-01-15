package com.jdevs.timeo.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.RecordsItemBinding
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.ui.common.adapter.ViewItem
import com.jdevs.timeo.ui.common.adapter.createViewModel
import com.jdevs.timeo.util.extensions.getFragmentActivity

class RecordDelegateAdapter : DelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        navigateToDetails: (Int) -> Unit,
        showDeleteDialog: (Int) -> Unit
    ): ViewHolder {

        val fragmentActivity = parent.getFragmentActivity()
        val inflater = LayoutInflater.from(fragmentActivity)

        val viewModel = createViewModel(fragmentActivity, RecordViewModel::class)

        val binding = RecordsItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = fragmentActivity
        }

        return ViewHolder(binding.root, viewModel, showDeleteDialog)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.bindRecord(item as Record)
    }

    class ViewHolder(
        private val rootView: View,
        private val viewModel: RecordViewModel,
        private val showDeleteDialog: (Int) -> Unit
    ) : PagingAdapter.ViewHolder(rootView) {

        init {

            viewModel.showDeleteDialog.observeEvent(lifecycleOwner) {
                showDeleteDialog(adapterPosition)
            }
        }

        fun bindRecord(record: Record) {

            val backgroundColorId =
                if (adapterPosition.rem(2) == 0) R.color.colorBlackTransparent else android.R.color.transparent

            rootView.setBackgroundColor(ContextCompat.getColor(context, backgroundColorId))
            viewModel.setRecord(record)
        }
    }
}
