package com.jdevs.timeo.ui.common

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Operation.ChangeType.Added
import com.jdevs.timeo.domain.model.Operation.ChangeType.Modified
import com.jdevs.timeo.domain.model.Operation.ChangeType.Removed
import com.jdevs.timeo.domain.model.Operation.Changed
import com.jdevs.timeo.domain.model.Operation.LastItemReached
import com.jdevs.timeo.domain.model.Operation.Successful
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.fragment.observe
import javax.inject.Inject

abstract class ListFragment<T : ViewItem>(@MenuRes menuId: Int = -1) : ActionBarFragment(menuId) {

    protected abstract val delegateAdapter: DelegateAdapter
    protected abstract val viewModel: ListViewModel<T>

    protected open val firestoreAdapter by lazy { FirestoreListAdapter(delegateAdapter) }
    private val adapter by lazy { PagingAdapter(delegateAdapter) }
    private val currentAdapter get() = if (authRepository.isSignedIn) firestoreAdapter else adapter

    @Inject
    lateinit var authRepository: AuthRepository

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (authRepository.isSignedIn) {
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

    private fun observeOperation(liveData: LiveData<Operation<T>>) {

        if (liveData.hasObservers()) return

        observe(liveData) { operation ->

            when (operation) {
                is Changed -> when (operation.changeType) {
                    Added -> firestoreAdapter.addItem(operation.item)
                    Modified -> firestoreAdapter.modifyItem(operation.item)
                    Removed -> firestoreAdapter.removeItem(operation.item)
                }

                is Successful -> {
                    viewModel.setLength(firestoreAdapter.dataItemCount)
                    firestoreAdapter.showLoader()
                }

                is LastItemReached -> firestoreAdapter.hideLoader()
            }
        }
    }

    protected fun RecyclerView.setup(visibleThreshold: Int) {

        val linearLayoutManager = LinearLayoutManager(context)
        layoutManager = linearLayoutManager
        adapter = currentAdapter

        if (authRepository.isSignedIn) {

            addOnScrollListener(InfiniteScrollListener(linearLayoutManager, visibleThreshold) {

                viewModel.getRemoteLiveDatas(true).forEach(::observeOperation)
            })
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getItem(position: Int) = if (authRepository.isSignedIn) {
        firestoreAdapter.getItem(position)
    } else {
        adapter.getItem(position)
    } as T
}
