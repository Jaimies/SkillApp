package com.maxpoliakov.skillapp.ui.common.history

import android.os.Bundle
import android.view.View
import androidx.annotation.MenuRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.ui.chart.BarChartFragment
import com.maxpoliakov.skillapp.ui.history.HistoryPagingAdapter
import com.maxpoliakov.skillapp.util.ui.addDividers
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class FragmentWithHistory<T : ViewDataBinding>(@MenuRes menuResId: Int) : BarChartFragment<T>(menuResId) {
    @Inject
    lateinit var listAdapter: HistoryPagingAdapter

    protected abstract val viewModel: ViewModelWithHistory

    protected abstract val T.recyclerView: RecyclerView

    override fun onBindingCreated(binding: T, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        binding.recyclerView.setupAdapter(listAdapter)
        binding.recyclerView.addDividers()

        lifecycleScope.launch {
            viewModel.records.collectLatest(listAdapter::submitData)
        }
        listAdapter.addLoadStateListener { loadStates ->
            if (loadStates.refresh !is LoadState.Loading) {
                if (listAdapter.itemCount == 0) onHistoryEmpty()
                else onHistoryNotEmpty()
            }
        }
    }

    override fun onPreDestroyBinding(binding: T) {
        super.onPreDestroyBinding(binding)
        binding.recyclerView.adapter = null
    }

    protected abstract fun onHistoryEmpty()
    protected abstract fun onHistoryNotEmpty()
}
