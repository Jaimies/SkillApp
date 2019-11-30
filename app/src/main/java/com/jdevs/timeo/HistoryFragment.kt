package com.jdevs.timeo

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.adapter.RecordsAdapter
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.FragmentHistoryBinding
import com.jdevs.timeo.models.ScrollDownListener
import com.jdevs.timeo.viewmodel.RecordListViewModel

class HistoryFragment : Fragment(),
    DialogInterface.OnClickListener,
    RecordListViewModel.Navigator {

    private val mAdapter by lazy { RecordsAdapter(::showDeleteDialog) }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RecordListViewModel::class.java).also {
            it.navigator = this
        }
    }

    private var chosenRecordIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHistoryBinding.inflate(inflater, container, false).also {

            it.viewmodel = viewModel
            it.lifecycleOwner = this

            it.recordsRecyclerView.apply {

                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter

                addOnScrollListener(ScrollDownListener(::getRecords))
            }
        }

        getRecords()

        return binding.root
    }

    override fun onDestroy() {

        super.onDestroy()
        viewModel.onFragmentDestroyed()
    }

    private fun showDeleteDialog(index: Int) {

        val dialog = AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.sure_delete_record))
            .setPositiveButton(getString(R.string.yes), this)
            .setNegativeButton(getString(R.string.no), null)

        chosenRecordIndex = index

        dialog.show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

        if (chosenRecordIndex == -1) {

            return
        }

        val record = getRecord(chosenRecordIndex)

        val recordTime = record.time

        Snackbar.make(view!!, getString(R.string.record_deleted), Snackbar.LENGTH_SHORT).show()

        viewModel.deleteRecord(mAdapter.getId(chosenRecordIndex), recordTime, record.activityId)
    }

    override fun onLastItemReached() {

        mAdapter.onLastItemReached()
    }

    private fun getRecords() {
        val recordsListLiveData = viewModel.recordsLiveData

        recordsListLiveData?.observe(this) { operation ->

            when (operation.type) {
                R.id.OPERATION_ADDED -> {
                    val record = operation.item ?: return@observe
                    mAdapter.addItem(record, operation.id)
                }

                R.id.OPERATION_MODIFIED -> {
                    val record = operation.item ?: return@observe
                    mAdapter.modifyItem(record, operation.id)
                }

                R.id.OPERATION_REMOVED -> {
                    mAdapter.removeItem(operation.id)
                }

                R.id.OPERATION_LOADED -> {
                    viewModel.onLoaded()
                }
            }

            viewModel.setLength(mAdapter.dataItemCount)
        }
    }

    private fun getRecord(index: Int): Record = mAdapter.getItem(index)
}
