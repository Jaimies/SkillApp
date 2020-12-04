package com.maxpoliakov.skillapp.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.time.toReadableDate

class SeparatorDelegateAdapter : DelegateAdapter<Separator, SeparatorDelegateAdapter.ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.separator_item, parent, false)
        return ViewHolder(view as TextView)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Separator) {
        holder.setData(item)
    }

    class ViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {
        fun setData(separator: Separator) {
            view.text = view.context.toReadableDate(separator.date)
        }
    }
}
