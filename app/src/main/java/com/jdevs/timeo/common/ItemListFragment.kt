package com.jdevs.timeo.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private val liveDatas = mutableListOf<ItemListLiveData>()
    private var isObserverAttached = false

    abstract fun getItems()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = super.onCreateView(inflater, container, savedInstanceState)

        if (!isObserverAttached) {

            getItems()
            isObserverAttached = true

            return view
        }

        liveDatas.forEach {

            if (!it.hasObservers()) {

                observe(it, shouldAddToList = false)
            }
        }

        return view
    }

    fun observe(liveData: ItemListLiveData?, shouldAddToList: Boolean = true) {

        if (liveData != null && shouldAddToList) {

            liveDatas.add(liveData)
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
