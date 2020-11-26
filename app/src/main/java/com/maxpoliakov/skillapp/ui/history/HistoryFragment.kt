package com.maxpoliakov.skillapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.HistoryFragBinding
import com.maxpoliakov.skillapp.ui.common.adapter.PagingAdapter
import com.maxpoliakov.skillapp.util.fragment.snackbar
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.history_frag.recycler_view

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private val delegateAdapter by lazy { RecordDelegateAdapter(::showDeleteDialog) }
    private val listAdapter by lazy { PagingAdapter(delegateAdapter) }
    val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = HistoryFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setupAdapter(listAdapter)
        viewModel.records.observe(viewLifecycleOwner, listAdapter::submitList)
    }

    private fun showDeleteDialog(index: Int) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_record_title)
            .setMessage(R.string.delete_record_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteRecord(listAdapter.getItem(index))
                snackbar(R.string.record_deleted_message)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
