package com.jdevs.timeo.ui.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.adapter.ViewTypeDelegateAdapter
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.RecordsItemBinding
import com.jdevs.timeo.ui.activities.viewmodel.RecordViewModel
import com.jdevs.timeo.util.randomString

class RecordDelegateAdapter :
    ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        goToDetails: (Int) -> Unit,
        deleteRecord: (Int) -> Unit
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = RecordsItemBinding.inflate(inflater, parent, false).also {

            it.viewmodel = ViewModelProviders.of(parent.context as FragmentActivity)
                .get(randomString(), RecordViewModel::class.java)

            it.lifecycleOwner = parent.context as FragmentActivity
        }

        return ViewHolder(binding, deleteRecord)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {

        holder as ViewHolder
        holder.bindRecord(item as Record)
    }

    class ViewHolder(
        private val binding: RecordsItemBinding,
        private val showDeleteDialog: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root),
        RecordViewModel.Navigator {

        init {

            binding.viewmodel?.navigator = this
        }

        fun bindRecord(record: Record) {

            val backgroundColorId =
                if (adapterPosition.rem(2) == 0) R.color.colorBlackTransparent else android.R.color.transparent

            binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    backgroundColorId
                )
            )

            binding.viewmodel?.setRecord(record)
        }

        override fun deleteRecord(view: View) = run {
            showDeleteDialog(adapterPosition)
            false
        }
    }
}
