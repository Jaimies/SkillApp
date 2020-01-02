package com.jdevs.timeo.ui.graphs

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.util.inflate
import kotlinx.android.synthetic.main.activitydetail_graphs_frag.view.title_text_view

class GraphsRecyclerViewAdapter(
    private val text: String
) : RecyclerView.Adapter<GraphsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.graph_list_item), text)

    @Suppress("EmptyFunctionBlock")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount() = 10

    class ViewHolder(view: View, text: String) : RecyclerView.ViewHolder(view) {

        init {

            view.title_text_view.text = text
        }
    }
}
