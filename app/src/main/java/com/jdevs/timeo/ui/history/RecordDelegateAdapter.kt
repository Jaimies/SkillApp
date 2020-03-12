package com.jdevs.timeo.ui.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.RecordsItemBinding
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.getColorCompat
import com.jdevs.timeo.util.view.fragmentActivity

class RecordDelegateAdapter(private val showDeleteDialog: (Int) -> Unit = {}) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val viewModel = RecordViewModel()

        val binding = RecordsItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = parent.fragmentActivity
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
    ) : BaseViewHolder(view) {

        init {

            viewModel.showDeleteDialog.observe { showDeleteDialog(adapterPosition) }
        }

        fun bindRecord(record: RecordItem) {

            val color = if (adapterPosition % 2 == 0)
                context.getColorCompat(R.color.colorBlackTransparent) else Color.TRANSPARENT

            view.setBackgroundColor(color)
            viewModel.setRecord(record)
        }
    }
}
