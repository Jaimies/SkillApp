package com.jdevs.timeo.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.HistoryFragBinding
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.util.fragment.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.history_frag.recycler_view

@AndroidEntryPoint
class HistoryFragment : ListFragment<RecordItem>(-1) {

    override val delegateAdapter by lazy { RecordDelegateAdapter(::showDeleteDialog) }

    override val viewModel: HistoryViewModel by viewModels()

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
        recycler_view.setup()
    }

    private fun showDeleteDialog(index: Int) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_record_title)
            .setMessage(R.string.delete_record_message)
            .setPositiveButton(R.string.delete) { _, _ ->

                viewModel.deleteRecord(getItem(index))
                snackbar(R.string.record_deleted_message)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
