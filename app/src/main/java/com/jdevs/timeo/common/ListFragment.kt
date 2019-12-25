package com.jdevs.timeo.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.remote.ItemsLiveData
import com.jdevs.timeo.util.OperationTypes.ADDED
import com.jdevs.timeo.util.OperationTypes.FAILED
import com.jdevs.timeo.util.OperationTypes.LAST_ITEM_REACHED
import com.jdevs.timeo.util.OperationTypes.MODIFIED
import com.jdevs.timeo.util.OperationTypes.REMOVED
import com.jdevs.timeo.util.OperationTypes.SUCCESSFUL
import com.jdevs.timeo.util.TAG

@Suppress("UNCHECKED_CAST")
abstract class ListFragment<T : ViewType> : ActionBarFragment() {

    protected abstract val viewModel: ListViewModel
    protected abstract val mAdapter: ListAdapter
    private val itemLiveDatas = mutableListOf<ItemsLiveData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        observeLiveData(viewModel.liveData)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observeLiveData(liveData: LiveData<*>?, forceFetchNewItems: Boolean = false) {

        if (liveData is ItemsLiveData?) {

            subscribeToItemsLiveData(liveData, forceFetchNewItems)
            return
        }

        liveData as LiveData<List<T>>

        liveData.observe(viewLifecycleOwner) {

            mAdapter.setItems(it)
            viewModel.setLength(it.size)
            viewModel.hideLoader()
        }
    }

    private fun subscribeToItemsLiveData(liveData: ItemsLiveData?, forceFetchNewItems: Boolean) {

        if (liveData != null && (itemLiveDatas.isEmpty() || forceFetchNewItems)) {

            observeItemsLiveData(liveData)
            return
        }

        itemLiveDatas.forEach {

            if (!it.hasObservers()) {

                observeItemsLiveData(it, shouldAddToList = false)
            }
        }
    }

    private fun observeItemsLiveData(liveData: ItemsLiveData, shouldAddToList: Boolean = true) {

        if (shouldAddToList) {

            itemLiveDatas.add(liveData)
        }

        liveData.observe(viewLifecycleOwner) { operation ->

            when (operation.type) {

                ADDED -> {

                    val item = operation.data as T
                    mAdapter.addItem(item)
                }

                MODIFIED -> {

                    val item = operation.data as T
                    mAdapter.modifyItem(item)
                }

                REMOVED -> {

                    val item = operation.data as T
                    mAdapter.removeItem(item)
                }

                SUCCESSFUL -> {

                    viewModel.setLength(mAdapter.dataItemCount)
                    viewModel.hideLoader()
                    mAdapter.showLoader()
                }

                LAST_ITEM_REACHED -> {

                    mAdapter.hideLoader()
                }

                FAILED -> {

                    Log.w(TAG, "Failed to get data from Firestore", operation.exception)
                }
            }
        }
    }

    protected fun RecyclerView.setup(visibleThreshold: Int) {

        val linearLayoutManager = LinearLayoutManager(context)

        layoutManager = linearLayoutManager
        adapter = mAdapter

        addOnScrollListener(
            InfiniteScrollListener(
                linearLayoutManager,
                visibleThreshold
            ) { observeLiveData(viewModel.liveData, forceFetchNewItems = true) }
        )
    }

    protected fun getItem(index: Int) = mAdapter.getItem(index) as T
}
