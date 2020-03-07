package com.jdevs.timeo.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.HistoryFragBinding
import com.jdevs.timeo.di.ViewModelFactory
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.snackbar
import kotlinx.android.synthetic.main.history_frag.recycler_view
import javax.inject.Inject

private const val RECORDS_VISIBLE_THRESHOLD = 12

class HistoryFragment : ListFragment<RecordItem>() {

    override val delegateAdapter by lazy { RecordDelegateAdapter(::showDeleteDialog) }
    override val firestoreAdapter by lazy { FirestoreHistoryAdapter(delegateAdapter) }

    override val viewModel: HistoryViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = HistoryFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycler_view.setup(RECORDS_VISIBLE_THRESHOLD)
    }

    private fun showDeleteDialog(index: Int) {

        AlertDialog.Builder(requireContext())
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(R.string.are_you_sure)
            .setMessage(R.string.sure_delete_record)
            .setPositiveButton(R.string.yes) { _, _ -> deleteRecord(index) }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun deleteRecord(index: Int) {

        viewModel.deleteRecord(record = getItem(index))
        snackbar(R.string.record_deleted)
    }
}
