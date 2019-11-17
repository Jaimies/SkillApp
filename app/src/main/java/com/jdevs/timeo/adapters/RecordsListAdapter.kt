package com.jdevs.timeo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.utils.Time
import kotlinx.android.synthetic.main.partial_records_item.view.nameTextView
import kotlinx.android.synthetic.main.partial_records_item.view.timeTextView

class RecordsListAdapter(
    private val recordList: List<Record>,
    private val showDeleteDialog: (Int) -> Unit = {}
) : RecyclerView.Adapter<RecordsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_records_item, parent, false) as ConstraintLayout

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRecord(recordList[position])
    }

    override fun getItemCount() = recordList.size

    inner class ViewHolder(view: ConstraintLayout) : RecyclerView.ViewHolder(view) {
        private val nameTextView = view.nameTextView
        private val timeTextView = view.timeTextView
        private val rootView = view.rootView

        init {
            view.setOnLongClickListener {
                showDeleteDialog(adapterPosition)
                true
            }
        }

        fun bindRecord(record: Record) {
            val backgroundColorId =
                if (adapterPosition.rem(2) == 0) R.color.colorListEven else R.color.colorListOdd

            rootView.setBackgroundColor(ContextCompat.getColor(rootView.context, backgroundColorId))

            nameTextView.apply {
                text = record.title
            }

            timeTextView.apply {
                text = Time.minsToTime(record.workingTime)
            }
        }
    }
}
