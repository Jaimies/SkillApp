package com.maxpoliakov.skillapp.ui.history

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.HistoryFragBinding
import com.maxpoliakov.skillapp.shared.history.FragmentWithHistory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : FragmentWithHistory<HistoryFragBinding>(-1) {
    override val layoutId get() = R.layout.history_frag

    override val viewModel: HistoryViewModel by viewModels()

    override val HistoryFragBinding.recyclerView get() = recyclerView

    override fun onBindingCreated(binding: HistoryFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onHistoryEmpty() {
        binding?.emptyListLayout?.root?.isVisible = true
    }

    override fun onHistoryNotEmpty() {
        binding?.emptyListLayout?.root?.isVisible = false
    }
}
