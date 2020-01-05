package com.jdevs.timeo.ui.history

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.R
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.common.ListFragment
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.HistoryFragBinding
import com.jdevs.timeo.util.RecordsConstants
import javax.inject.Inject

class HistoryFragment : ListFragment<Record>(), DialogInterface.OnClickListener {

    override val menuId = R.menu.history_fragment_menu
    override val adapter by lazy { RecordsAdapter(::showDeleteDialog) }
    override val firestoreAdapter by lazy { FirestoreRecordsAdapter(::showDeleteDialog) }
    private var chosenItemIndex = -1

    @Inject
    override lateinit var viewModel: HistoryViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        (activity!!.application as TimeoApplication).appComponent.inject(this)
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

        Snackbar.make(view!!, getString(R.string.record_deleted), Snackbar.LENGTH_SHORT).show()
    }
}
