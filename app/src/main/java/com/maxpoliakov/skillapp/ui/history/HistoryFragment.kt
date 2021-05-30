package com.maxpoliakov.skillapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.maxpoliakov.skillapp.databinding.HistoryFragBinding
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    val viewModel: HistoryViewModel by viewModels()

    @Inject
    lateinit var listAdapter: HistoryPagingAdapter

    private lateinit var binding: HistoryFragBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = HistoryFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.setupAdapter(listAdapter)
        lifecycleScope.launch {
            viewModel.records.collectLatest(listAdapter::submitData)
        }
        listAdapter.addLoadStateListener { loadStates ->
            if (loadStates.refresh !is LoadState.Loading)
                binding.emptyText.isVisible = listAdapter.itemCount == 0
        }
    }
}
