package com.jdevs.timeo.ui.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ListFragment
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.HistoryFragBinding
import com.jdevs.timeo.ui.activities.adapter.RecordsAdapter
import com.jdevs.timeo.ui.activities.viewmodel.HistoryViewModel
import com.jdevs.timeo.util.RecordsConstants

class HistoryFragment : ListFragment<Record>(), DialogInterface.OnClickListener {

    override val menuId = R.menu.history_fragment_menu
    override val viewModel: HistoryViewModel by viewModels()
    override val mAdapter by lazy { RecordsAdapter(::showDeleteDialog) }

    private var chosenItemIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

        viewModel.deleteRecord(
            id = mAdapter.getId(chosenItemIndex),
            record = getRecord(chosenItemIndex)
        )

        chosenItemIndex = -1

        Snackbar.make(view!!, getString(R.string.record_deleted), Snackbar.LENGTH_SHORT).show()
    }

    private fun getRecord(index: Int) = mAdapter.getItem(index) as Record
}
