package com.jdevs.timeo.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.common.adapter.DelegateAdapter
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.data.DataItem
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.RecordsItemBinding
import java.util.UUID

class RecordDelegateAdapter : DelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        goToDetails: (Int) -> Unit,
        showDeleteDialog: (Int) -> Unit
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.context as FragmentActivity

        val viewModel = ViewModelProvider(fragmentActivity)
            .get(UUID.randomUUID().toString(), RecordViewModel::class.java)

        val binding = RecordsItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = fragmentActivity
        }

        return ViewHolder(binding.root, viewModel, showDeleteDialog)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DataItem) {

        holder as ViewHolder
        holder.bindRecord(item as Record)
    }

    class ViewHolder(
        private val rootView: View,
        private val viewModel: RecordViewModel,
        private val showDeleteDialog: (Int) -> Unit
    ) : ListAdapter.ViewHolder(rootView) {

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
