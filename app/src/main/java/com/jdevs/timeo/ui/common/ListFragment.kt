package com.jdevs.timeo.ui.common

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.model.OperationItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.shared.OperationType.ADDED
import com.jdevs.timeo.shared.OperationType.LAST_ITEM_REACHED
import com.jdevs.timeo.shared.OperationType.MODIFIED
import com.jdevs.timeo.shared.OperationType.REMOVED
import com.jdevs.timeo.shared.OperationType.SUCCESSFUL
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.fragment.observe
import javax.inject.Inject

abstract class ListFragment<T : ViewItem> : ActionBarFragment() {

    protected abstract val delegateAdapter: DelegateAdapter
    protected abstract val viewModel: ListViewModel<T>

    protected open val firestoreAdapter by lazy { FirestoreListAdapter(delegateAdapter) }
    private val adapter by lazy { PagingAdapter(delegateAdapter) }
    private val currentAdapter get() = if (authRepository.isUserSignedIn) firestoreAdapter else adapter

    @Inject
    lateinit var authRepository: AuthRepository

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (authRepository.isUserSignedIn) {
            viewModel.getRemoteLiveDatas(false).forEach(::observeOperation)
        } else {
            observeLiveData(viewModel.localLiveData)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeLiveData(liveData: LiveData<PagedList<T>>) {

        observe(liveData) {
            adapter.submitList(it as PagedList<ViewItem>)
            viewModel.setLength(it.size)
            viewModel.hideLoader()
        }
    }

    private fun observeOperation(liveData: LiveData<OperationItem<T>>) {

        if (liveData.hasObservers()) {
            return
        }

        observe(liveData) { operation ->

            when (operation.type) {

                ADDED -> firestoreAdapter.addItem(operation.data!!)
                MODIFIED -> firestoreAdapter.modifyItem(operation.data!!)
                REMOVED -> firestoreAdapter.removeItem(operation.data!!)

                SUCCESSFUL -> {
                    viewModel.setLength(firestoreAdapter.dataItemCount)
                    firestoreAdapter.showLoader()
                }

                LAST_ITEM_REACHED -> firestoreAdapter.hideLoader()
            }
        }
    }

    protected fun RecyclerView.setup(visibleThreshold: Int) {

        val linearLayoutManager = LinearLayoutManager(context)
        layoutManager = linearLayoutManager
        adapter = currentAdapter

        if (authRepository.isUserSignedIn) {

            addOnScrollListener(InfiniteScrollListener(linearLayoutManager, visibleThreshold) {

                viewModel.getRemoteLiveDatas(true).forEach(::observeOperation)
            })
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getItem(position: Int) = if (authRepository.isUserSignedIn) {
        firestoreAdapter.getItem(position)
    } else {
        adapter.getItem(position)
    } as T
}
