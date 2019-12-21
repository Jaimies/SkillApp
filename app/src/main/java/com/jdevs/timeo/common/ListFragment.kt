package com.jdevs.timeo.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.ItemsLiveData
import com.jdevs.timeo.util.OperationTypes.ADDED
import com.jdevs.timeo.util.OperationTypes.FAILED
import com.jdevs.timeo.util.OperationTypes.FINISHED
import com.jdevs.timeo.util.OperationTypes.MODIFIED
import com.jdevs.timeo.util.OperationTypes.REMOVED
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.observeEvent

@Suppress("UNCHECKED_CAST")
abstract class ListFragment<T : ViewType> : ActionBarFragment() {

    protected abstract val viewModel: ListViewModel
    protected abstract val mAdapter: ListAdapter
    private val itemLiveDatas = mutableListOf<ItemsLiveData>()
    private var hasObserverAttached = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        observeEvent(viewModel.onLastItemReached) {

            mAdapter.onLastItemReached()
        }

        if (!hasObserverAttached) {

            observe(viewModel.liveData)
            hasObserverAttached = true
        } else {

            itemLiveDatas.forEach {

                if (!it.hasObservers()) {

                    observe(it, shouldAddToList = false)
                }
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observe(liveData: ItemsLiveData?, shouldAddToList: Boolean = true) {

        if (liveData != null && shouldAddToList) {

            itemLiveDatas.add(liveData)
        }

        liveData?.observe(viewLifecycleOwner) { operation ->

            when (operation.type) {

                ADDED -> {

                    val item = operation.item as T
                    mAdapter.addItem(item, operation.id)
                }

                MODIFIED -> {

                    val item = operation.item as T
                    mAdapter.modifyItem(item, operation.id)
                }

                REMOVED -> {

                    mAdapter.removeItem(operation.id)
                }

                FINISHED -> {

                    viewModel.hideLoader()
                    mAdapter.showLoader()

                    viewModel.setLength(mAdapter.dataItemCount)
                }

                FAILED -> {

                    Log.w(TAG, "Failed to get data from Firestore", operation.exception)
                }
            }
        }
    }

    fun RecyclerView.setup(visibleThreshold: Int) {

        val linearLayoutManager = LinearLayoutManager(context)

        layoutManager = linearLayoutManager
        adapter = mAdapter

        addOnScrollListener(
            InfiniteScrollListener(
                linearLayoutManager,
                visibleThreshold
            ) { observe(viewModel.liveData) }
        )
    }

    protected fun getItem(index: Int) = mAdapter.getItem(index) as T
}
