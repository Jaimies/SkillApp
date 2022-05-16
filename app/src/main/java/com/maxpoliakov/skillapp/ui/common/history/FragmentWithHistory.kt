package com.maxpoliakov.skillapp.ui.common.history

import android.os.Bundle
import android.view.View
import androidx.annotation.MenuRes
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.ui.common.BarChartFragment
import com.maxpoliakov.skillapp.ui.history.HistoryPagingAdapter
import com.maxpoliakov.skillapp.util.ui.addDividers
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class FragmentWithHistory(@MenuRes menuResId: Int) : BarChartFragment(menuResId) {
    @Inject
    lateinit var listAdapter: HistoryPagingAdapter

    protected abstract val viewModel: ViewModelWithHistory
    abstract val recyclerView: RecyclerView
    abstract val emptyListLayout: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setupAdapter(listAdapter)
        recyclerView.addDividers()
        lifecycleScope.launch {
            viewModel.records.collectLatest(listAdapter::submitData)
        }
        listAdapter.addLoadStateListener { loadStates ->
            if (loadStates.refresh !is LoadState.Loading)
                emptyListLayout.isVisible = listAdapter.itemCount == 0
        }
    }
}
