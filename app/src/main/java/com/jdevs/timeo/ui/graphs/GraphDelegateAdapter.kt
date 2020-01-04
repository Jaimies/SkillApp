package com.jdevs.timeo.ui.graphs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.DelegateAdapter
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.data.Stats
import com.jdevs.timeo.databinding.GraphsItemBinding
import java.util.UUID

class GraphDelegateAdapter : DelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        goToDetails: (Int) -> Unit,
        showDeleteDialog: (Int) -> Unit
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.context as FragmentActivity

        val viewModel = ViewModelProvider(fragmentActivity).get(
            UUID.randomUUID().toString(), GraphViewModel::class.java
        )

        val binding = GraphsItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = fragmentActivity
        }

        return ViewHolder(viewModel, binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.bindStats(item as Stats)
    }

    class ViewHolder(
        private val viewModel: GraphViewModel,
        view: View
    ) : ListAdapter.ViewHolder(view) {

        fun bindStats(stats: Stats) = viewModel.setStats(stats)
    }
}
