package com.jdevs.timeo.ui.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.DelegateAdapter
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.common.adapter.createViewModel
import com.jdevs.timeo.databinding.StatsItemBinding
import com.jdevs.timeo.model.Stats
import com.jdevs.timeo.util.getFragmentActivity

class StatisticDelegateAdapter : DelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        navigateToDetails: (Int) -> Unit,
        showDeleteDialog: (Int) -> Unit
    ): RecyclerView.ViewHolder {

        val fragmentActivity = parent.getFragmentActivity()
        val inflater = LayoutInflater.from(fragmentActivity)

        val viewModel = createViewModel(fragmentActivity, StatisticViewModel::class)

        val binding = StatsItemBinding.inflate(inflater, parent, false).also {

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
        private val viewModel: StatisticViewModel,
        view: View
    ) : ListAdapter.ViewHolder(view) {

        fun bindStats(stats: Stats) = viewModel.setStats(stats)
    }
}
