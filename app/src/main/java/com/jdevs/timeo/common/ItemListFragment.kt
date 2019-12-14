package com.jdevs.timeo.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.common.adapter.ItemListAdapter
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.viewmodel.ItemListViewModel
import com.jdevs.timeo.data.livedata.ItemListLiveData
import com.jdevs.timeo.util.OperationStates.ADDED
import com.jdevs.timeo.util.OperationStates.FAILED
import com.jdevs.timeo.util.OperationStates.FINISHED
import com.jdevs.timeo.util.OperationStates.MODIFIED
import com.jdevs.timeo.util.OperationStates.REMOVED
import com.jdevs.timeo.util.TAG

@Suppress("UNCHECKED_CAST")
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

                FAILED -> {

                    Log.w(TAG, "Failed to get data from Firestore", operation.exception)
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
