package com.jdevs.timeo.common

import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.api.livedata.ItemListLiveData
import com.jdevs.timeo.common.adapter.ItemListAdapter
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.viewmodel.ItemListViewModel
import com.jdevs.timeo.util.OperationConstants.ADDED
import com.jdevs.timeo.util.OperationConstants.FINISHED
import com.jdevs.timeo.util.OperationConstants.MODIFIED
import com.jdevs.timeo.util.OperationConstants.REMOVED

abstract class ItemListFragment<T : ViewType> : ActionBarFragment(),
    ItemListViewModel.Navigator {

    protected abstract val viewModel: ItemListViewModel
    protected abstract val mAdapter: ItemListAdapter
    protected lateinit var linearLayoutManager: LinearLayoutManager

    private var isObserverAttached = false

    abstract fun getItems()

    override fun onStart() {

        super.onStart()

        if (!isObserverAttached) {

            getItems()
            isObserverAttached = true
        }
    }

    fun observe(liveData: ItemListLiveData?) {

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
            }
        }
    }

    override fun onDestroy() {

        super.onDestroy()
        viewModel.onFragmentDestroyed()
    }

    override fun onLastItemReached() {

        mAdapter.onLastItemReached()
    }
}
