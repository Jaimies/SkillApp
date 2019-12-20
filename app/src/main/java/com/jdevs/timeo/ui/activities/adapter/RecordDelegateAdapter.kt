package com.jdevs.timeo.ui.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.adapter.ViewTypeDelegateAdapter
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.RecordsItemBinding
import com.jdevs.timeo.ui.activities.viewmodel.RecordViewModel
import com.jdevs.timeo.util.randomString

class RecordDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        goToDetails: (Int) -> Unit,
        deleteRecord: (Int) -> Unit
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.context as FragmentActivity

        val viewModel =
            ViewModelProviders.of(fragmentActivity).get(randomString(), RecordViewModel::class.java)

        val binding = RecordsItemBinding.inflate(inflater, parent, false).also {

            it.viewmodel = viewModel
            it.lifecycleOwner = fragmentActivity
        }

        return ViewHolder(binding.root, viewModel, deleteRecord)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {

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
