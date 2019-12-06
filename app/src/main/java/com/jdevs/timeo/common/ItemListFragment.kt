package com.jdevs.timeo.common

import android.os.Bundle
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.R
import com.jdevs.timeo.api.livedata.ItemListLiveData
import com.jdevs.timeo.common.adapter.ItemListAdapter
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.viewmodel.ItemListViewModel

abstract class ItemListFragment<T : ViewType> : ActionBarFragment(),
    ItemListViewModel.Navigator {
    protected abstract val viewModel: ItemListViewModel
    protected abstract val mAdapter: ItemListAdapter

    protected lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.setupRepository()
    }

    fun observeOperation(liveData: ItemListLiveData?) {
        liveData?.observe(viewLifecycleOwner) { operation ->

            when (operation.type) {

                R.id.OPERATION_FINISHED -> {
                    viewModel.hideLoader()
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
