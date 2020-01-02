package com.jdevs.timeo.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.remote.ItemsLiveData
import com.jdevs.timeo.util.OperationTypes.ADDED
import com.jdevs.timeo.util.OperationTypes.FAILED
import com.jdevs.timeo.util.OperationTypes.LAST_ITEM_REACHED
import com.jdevs.timeo.util.OperationTypes.MODIFIED
import com.jdevs.timeo.util.OperationTypes.REMOVED
import com.jdevs.timeo.util.OperationTypes.SUCCESSFUL
import com.jdevs.timeo.util.TAG
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
abstract class ListFragment<T : ViewItem> : ActionBarFragment() {

    protected abstract val viewModel: ListViewModel
    private val itemLiveDatas = mutableListOf<ItemsLiveData>()

    protected abstract val roomAdapter: ListAdapter
    protected abstract val firestoreAdapter: FirestoreListAdapter
    private val currentAdapter get() = if (authRepository.isUserSignedIn) firestoreAdapter else roomAdapter

    @Inject
    lateinit var authRepository: AuthRepository

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        observeLiveData(viewModel.liveData)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observeLiveData(liveData: LiveData<*>?) {

        if (liveData is ItemsLiveData?) {

            subscribeToItemsLiveData(liveData)
            return
        }

        liveData as LiveData<PagedList<ViewItem>>

        liveData.observe(viewLifecycleOwner) {

            roomAdapter.submitList(it)
            viewModel.setLength(it.size)
            viewModel.hideLoader()
        }
    }

    private fun subscribeToItemsLiveData(liveData: ItemsLiveData?) {

        if (liveData != null && itemLiveDatas.isEmpty()) {

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
                    firestoreAdapter.addItem(item)
                }

                MODIFIED -> {

                    val item = operation.data as T
                    firestoreAdapter.modifyItem(item)
                }

                REMOVED -> {

                    val item = operation.data as T
                    firestoreAdapter.removeItem(item)
                }

                SUCCESSFUL -> {

                    viewModel.setLength(firestoreAdapter.dataItemCount)
                    viewModel.hideLoader()
                    firestoreAdapter.showLoader()
                }

                LAST_ITEM_REACHED -> {

                    firestoreAdapter.hideLoader()
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

        adapter = currentAdapter

        if (authRepository.isUserSignedIn) {

            addOnScrollListener(
                InfiniteScrollListener(
                    linearLayoutManager,
                    visibleThreshold
                ) {

                    val liveData = viewModel.liveData as ItemsLiveData?

                    observeItemsLiveData(liveData ?: return@InfiniteScrollListener)
                }
            )
        }
    }

    protected fun getItem(position: Int): T {

        return if (authRepository.isUserSignedIn) {

            firestoreAdapter.getItem(position)
        } else {

            roomAdapter.getItem(position)
        } as T
    }
}
