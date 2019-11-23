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
import com.jdevs.timeo.adapters.RecordsListAdapter
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.RecordOperation
import com.jdevs.timeo.databinding.FragmentHistoryBinding
import com.jdevs.timeo.models.ScrollDownListener
import com.jdevs.timeo.viewmodels.RecordListViewModel

class HistoryFragment : Fragment(),
    DialogInterface.OnClickListener {

    private val recordList = ArrayList<Record>()
    private val idList = ArrayList<String>()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RecordListViewModel::class.java)
    }

    private val mAdapter by lazy {
        RecordsListAdapter(recordList, ::showDeleteDialog)
    }

    private var chosenRecordIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.recordsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addOnScrollListener(ScrollDownListener(::getRecords))
        }

        getRecords()

        return binding.root
    }

    private fun showDeleteDialog(index: Int) {

        val dialog = AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle("Are you sure?")
            .setMessage("Are you sure you want to delete this record?")
            .setPositiveButton("Yes", this)
            .setNegativeButton("No", null)

        chosenRecordIndex = index

        dialog.show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

        if (chosenRecordIndex == -1) {

            return
        }

        val recordTime = recordList[chosenRecordIndex].time.toLong()

        val snack =
            Snackbar.make(view!!, "Record deleted", Snackbar.LENGTH_SHORT)
        snack.show()

        viewModel.deleteRecord(
            idList[chosenRecordIndex],
            recordTime,
            recordList[chosenRecordIndex].activityId
        )
    }

    private fun getRecords() {
        val recordsListLiveData = viewModel.recordsListLiveData ?: return

        recordsListLiveData.observe(this) { operation: RecordOperation ->

            when (operation.type) {
                R.id.OPERATION_ADDED -> {
                    val record = operation.activity ?: return@observe
                    addRecord(record, operation.id)
                }

                R.id.OPERATION_MODIFIED -> {
                    val record = operation.activity ?: return@observe
                    modifyRecord(record, operation.id)
                }

                R.id.OPERATION_REMOVED -> {
                    removeRecord(operation.id)
                }

                R.id.OPERATION_LOADED -> {
                    viewModel.onLoaded()
                }
            }

            viewModel.setLength(recordList.size)
        }
    }

    private fun addRecord(record: Record, id: String) {
        recordList.add(record)
        idList.add(id)
        mAdapter.notifyItemInserted(recordList.size - 1)
    }

    private fun removeRecord(id: String) {

        val index = recordList.withIndex().filterIndexed { index, _ -> idList[index] == id }
            .map { it.index }
            .first()

        recordList.removeAt(index)
        idList.remove(id)

        mAdapter.notifyItemRemoved(index)
    }

    private fun modifyRecord(record: Record, id: String) {
        val index =
            recordList.withIndex().filterIndexed { index, _ -> idList[index] == id }
                .map { it.index }
                .first()

        recordList[index] = record

        mAdapter.notifyItemChanged(index)
    }
}
