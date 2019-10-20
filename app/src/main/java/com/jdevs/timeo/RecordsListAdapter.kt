package com.jdevs.timeo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.partial_records_list_item.view.*

class RecordsListAdapter(private val dataset: Array<String>) : RecyclerView.Adapter<RecordsListAdapter.ViewHolder>() {

    class ViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_records_list_item, parent, false) as LinearLayout

        return ViewHolder(layout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layout.activityNameTextView.apply {

            text = dataset[position]

        }

    }

    override fun getItemCount() = dataset.size
}