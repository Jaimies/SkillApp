package com.jdevs.timeo.common

import androidx.lifecycle.observe
import com.jdevs.timeo.R
import com.jdevs.timeo.api.livedata.ItemListLiveData
import com.jdevs.timeo.common.adapter.ItemListAdapter
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.viewmodel.ItemListViewModel

abstract class ItemListFragment<T : ViewType> : ActionBarFragment(),
    ItemListViewModel.Navigator {
    protected abstract val viewModel: ItemListViewModel
    protected abstract val mAdapter: ItemListAdapter

    fun observeOperation(liveData: ItemListLiveData?) {
        liveData?.observe(viewLifecycleOwner) { operation ->

            when (operation.type) {
                R.id.OPERATION_LOADED -> {
                    viewModel.hideLoader()
                }

                R.id.OPERATION_FINISHED -> {
                    mAdapter.showLoader()
                    viewModel.setLength(mAdapter.dataItemCount)
                }

                R.id.OPERATION_ADDED -> {
                    val item = operation.item as T
                    mAdapter.addItem(item, operation.id)
                }

                R.id.OPERATION_MODIFIED -> {
                    val item = operation.item as T
                    mAdapter.modifyItem(item, operation.id)
                }

                R.id.OPERATION_REMOVED -> {
                    mAdapter.removeItem(operation.id)
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
