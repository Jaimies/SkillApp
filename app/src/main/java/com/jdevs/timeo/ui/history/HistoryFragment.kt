package com.jdevs.timeo.ui.history

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.HistoryFragBinding
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.showSnackbar
import javax.inject.Inject

private const val RECORDS_VISIBLE_THRESHOLD = 12

class HistoryFragment : ListFragment<RecordItem>(), DialogInterface.OnClickListener {

    override val adapter by lazy {
        PagingAdapter(RecordDelegateAdapter(), showDeleteDialog = ::showDeleteDialog)
    }

    override val firestoreAdapter by lazy { FirestoreListAdapter(showDeleteDialog = ::showDeleteDialog) }

    @Inject
    override lateinit var viewModel: HistoryViewModel

    private var chosenItemIndex = -1
    override val menuId = -1

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

            it.viewModel = viewModel
            it.lifecycleOwner = this
            it.recyclerView.setup(RECORDS_VISIBLE_THRESHOLD)
        }

        return binding.root
    }

    private fun showDeleteDialog(index: Int) {

        chosenItemIndex = index

        AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(R.string.are_you_sure)
            .setMessage(R.string.sure_delete_record)
            .setPositiveButton(R.string.yes, this)
            .setNegativeButton(R.string.no, null)
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
