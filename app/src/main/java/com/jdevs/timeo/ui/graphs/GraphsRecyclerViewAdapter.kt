package com.jdevs.timeo.ui.graphs

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.util.inflate

class GraphsRecyclerViewAdapter(private val onClick: (View) -> Unit = {}) :
    RecyclerView.Adapter<GraphsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.graph_list_item), onClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount() = 10

    class ViewHolder(view: View, onClick: (View) -> Unit = {}) : RecyclerView.ViewHolder(view) {

        init {

            view.setOnClickListener(onClick)
        }
    }
}
