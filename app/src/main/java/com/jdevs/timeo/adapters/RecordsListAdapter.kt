package com.jdevs.timeo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.data.TimeoRecord
import com.jdevs.timeo.utilities.TimeUtility
import kotlinx.android.synthetic.main.partial_records_list_item.view.activityNameTextView
import kotlinx.android.synthetic.main.partial_records_list_item.view.workTimeTextView

class RecordsListAdapter(
    private val dataset: ArrayList<TimeoRecord>,
    private val showDeleteDialog: (Int) -> Unit = {}
) : RecyclerView.Adapter<RecordsListAdapter.ViewHolder>() {

    inner class ViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout),
        View.OnLongClickListener {

        init {
            layout.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View): Boolean {

            showDeleteDialog(adapterPosition)

            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_records_list_item, parent, false) as ConstraintLayout

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layout.apply {

            val backgroundColorId =
                if (position.rem(2) == 0) R.color.colorListEven else R.color.colorListOdd

            setBackgroundColor(ContextCompat.getColor(context, backgroundColorId))

            activityNameTextView.apply {

                text = dataset[position].title
            }

            workTimeTextView.apply {

                text = TimeUtility.minsToTime(dataset[position].workingTime)
            }
        }
    }

    override fun getItemCount() = dataset.size

    interface RecordsListEventListener {
        fun showDeleteDialog(index: Int)
    }
}
