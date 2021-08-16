package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter

class EmptyDelegateAdapter : DelegateAdapter<Nothing, RecyclerView.ViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return EmptyViewHolder(View(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Nothing) {}

    private class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
