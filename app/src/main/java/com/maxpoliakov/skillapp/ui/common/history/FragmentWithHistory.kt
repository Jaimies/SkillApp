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
    abstract val recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setupAdapter(listAdapter)
        recyclerView.addDividers()
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

    override fun onPreDestroyBinding() {
        recyclerView.adapter = null
    }

    protected abstract fun onHistoryEmpty()
    protected abstract fun onHistoryNotEmpty()
}
