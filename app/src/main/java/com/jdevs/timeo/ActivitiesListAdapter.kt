package com.jdevs.timeo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.partial_activities_list_item.view.*

class ActivitiesListAdapter(private val dataset: Array<String>) : RecyclerView.Adapter<ActivitiesListAdapter.ViewHolder>() {

    class ViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_activities_list_item, parent, false) as LinearLayout

        return ViewHolder(layout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layout.listItemTitle.apply {

            text = dataset[position]

        }

    }

    override fun getItemCount() = dataset.size
}