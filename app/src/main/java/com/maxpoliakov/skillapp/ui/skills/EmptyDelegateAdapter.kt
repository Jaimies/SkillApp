package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter

class EmptyDelegateAdapter : DelegateAdapter<Nothing, RecyclerView.ViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = View(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(parent.width, 1)
        }
        return EmptyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Nothing) {}

}

class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
