package com.jdevs.timeo.ui.common

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
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.model.OperationItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.shared.OperationTypes.ADDED
import com.jdevs.timeo.shared.OperationTypes.FAILED
import com.jdevs.timeo.shared.OperationTypes.LAST_ITEM_REACHED
import com.jdevs.timeo.shared.OperationTypes.MODIFIED
import com.jdevs.timeo.shared.OperationTypes.REMOVED
import com.jdevs.timeo.shared.OperationTypes.SUCCESSFUL
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.TAG
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
abstract class ListFragment<T : ViewItem> : ActionBarFragment() {

    protected abstract val viewModel: ListViewModel<T>
    protected abstract val adapter: PagingAdapter
    protected abstract val firestoreAdapter: FirestoreListAdapter
    private val currentAdapter get() = if (authRepository.isUserSignedIn) firestoreAdapter else adapter

    @Inject
    lateinit var authRepository: AuthRepository

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (authRepository.isUserSignedIn) {

            for (liveData in viewModel.remoteLiveDatas) {

                observeItemsLiveData(liveData)
            }
        } else {

            observeLiveData(viewModel.localLiveData)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observeLiveData(liveData: LiveData<out PagedList<*>>) {

        liveData as LiveData<PagedList<ViewItem>>

        liveData.observe(viewLifecycleOwner) {

            adapter.submitList(it)
            viewModel.setLength(it.size)
            viewModel.hideLoader()
        }
    }

    private fun observeItemsLiveData(liveData: LiveData<out OperationItem<out T>>) {

        if (liveData.hasObservers()) {

            return
        }

        liveData.observe(viewLifecycleOwner) { operation ->

            when (operation.type) {

                ADDED -> firestoreAdapter.addItem(operation.data!!)
                MODIFIED -> firestoreAdapter.modifyItem(operation.data!!)
                REMOVED -> firestoreAdapter.removeItem(operation.data!!)

                SUCCESSFUL -> {

                    viewModel.setLength(firestoreAdapter.dataItemCount)
                    viewModel.hideLoader()
                    firestoreAdapter.showLoader()
                }

                LAST_ITEM_REACHED -> firestoreAdapter.hideLoader()
                FAILED -> Log.w(TAG, "Failed to get data from Firestore", operation.exception)
            }
        }
    }

    protected fun RecyclerView.setup(visibleThreshold: Int) {

        val linearLayoutManager = LinearLayoutManager(context)
        layoutManager = linearLayoutManager
        adapter = currentAdapter

        if (authRepository.isUserSignedIn) {

            addOnScrollListener(InfiniteScrollListener(linearLayoutManager, visibleThreshold) {

                viewModel.remoteLiveDatas.forEach(::observeItemsLiveData)
            })
        }
    }

    protected fun getItem(position: Int) = if (authRepository.isUserSignedIn) {

        firestoreAdapter.getItem(position)
    } else {

        adapter.getItem(position)
    } as T
}
