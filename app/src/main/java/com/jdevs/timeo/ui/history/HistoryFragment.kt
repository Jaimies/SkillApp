package com.jdevs.timeo.ui.history

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ListFragment
import com.jdevs.timeo.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.databinding.HistoryFragBinding
import com.jdevs.timeo.model.Record
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.extensions.getAppComponent
import com.jdevs.timeo.util.extensions.showSnackbar
import javax.inject.Inject

class HistoryFragment : ListFragment<Record>(), DialogInterface.OnClickListener {

    override val adapter by lazy {

        ListAdapter(RecordDelegateAdapter(), showDeleteDialog = ::showDeleteDialog)
    }

    override val firestoreAdapter by lazy { FirestoreListAdapter(showDeleteDialog = ::showDeleteDialog) }

    override val menuId = R.menu.history_fragment_menu
    private var chosenItemIndex = -1

    @Inject
    override lateinit var viewModel: HistoryViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = HistoryFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
            it.recyclerView.setup(RecordsConstants.VISIBLE_THRESHOLD)
        }

        return binding.root
    }

    private fun showDeleteDialog(index: Int) {

        chosenItemIndex = index

        AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.sure_delete_record))
            .setPositiveButton(getString(R.string.yes), this)
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

        if (chosenItemIndex == -1) {

            return
        }

        viewModel.deleteRecord(record = getItem(chosenItemIndex))
        chosenItemIndex = -1

        showSnackbar(R.string.record_deleted)
    }
}
