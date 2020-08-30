package com.jdevs.timeo.ui.common

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.ui.setupAdapter

abstract class ListFragment<T : ViewItem>(@MenuRes menuId: Int = -1) :
    ActionBarFragment(menuId) {

    protected abstract val delegateAdapter: DelegateAdapter
    protected abstract val viewModel: ListViewModel<T>

    private val listAdapter by lazy { PagingAdapter(delegateAdapter) }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeLiveData(viewModel.liveData)
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeLiveData(liveData: LiveData<PagedList<T>>) {
        observe(liveData) {
            listAdapter.submitList(it as PagedList<ViewItem>)
            viewModel.setLength(it.size)
        }
    }

    protected fun RecyclerView.setup() {
        setupAdapter(listAdapter)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getItem(position: Int): T {
        return listAdapter.getItem(position) as T
    }
}
