package com.jdevs.timeo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.RecordsItemBinding
import com.jdevs.timeo.util.randomString
import com.jdevs.timeo.viewmodel.RecordViewModel

class RecordAdapter(
    private val recordList: List<Record>,
    private val showDeleteDialog: (Int) -> Unit = {}
) : RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RecordsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).also {

                it.viewmodel = ViewModelProviders.of(parent.context as FragmentActivity)
                    .get(randomString(), RecordViewModel::class.java)

                it.lifecycleOwner = parent.context as FragmentActivity
            }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRecord(recordList[position])
    }

    override fun getItemCount() = recordList.size

    inner class ViewHolder(private val binding: RecordsItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        RecordViewModel.Navigator {

        init {
            binding.viewmodel?.navigator = this
        }

        fun bindRecord(record: Record) {
            val backgroundColorId =
                if (adapterPosition.rem(2) == 0) R.color.colorListEven else R.color.colorListOdd

            binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    backgroundColorId
                )
            )

            binding.viewmodel?.setRecord(record)
        }

        override fun deleteRecord(view: View): Boolean {
            showDeleteDialog(adapterPosition)
            return false
        }
    }
}
