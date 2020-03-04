package com.jdevs.timeo.ui.history

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.view.inflate

class DateLabelDelegateAdapter : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflate(R.layout.date_label) as TextView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {
        holder as ViewHolder
        holder.setData(item as DateLabel)
    }

    class ViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {

        fun setData(label: DateLabel) {
            textView.text = label.dateString
        }
    }
}
