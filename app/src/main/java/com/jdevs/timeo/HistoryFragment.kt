package com.jdevs.timeo

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.adapters.RecordsListAdapter
import com.jdevs.timeo.data.RecordOperation
import com.jdevs.timeo.data.TimeoRecord
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.models.RealtimeScrollListener
import com.jdevs.timeo.viewmodels.RecordsListViewModel
import kotlinx.android.synthetic.main.partial_circular_loader.view.spinningProgressBar
import kotlinx.android.synthetic.main.partial_records_list.view.createNewActivityTextView
import kotlinx.android.synthetic.main.partial_records_list.view.recordsRecyclerView

class HistoryFragment : ActionBarFragment(),
    DialogInterface.OnClickListener {

    private val mRecords = ArrayList<TimeoRecord>()
    private val mItemIds = ArrayList<String>()

    private var recordsListViewModel: RecordsListViewModel? = null

    private lateinit var mViewAdapter: RecordsListAdapter

    private lateinit var mLoader: FrameLayout
    private lateinit var mRecordsRecyclerView: RecyclerView
    private lateinit var mCreateNewActivityView: TextView

    private var chosenRecordIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        mLoader = view.spinningProgressBar.apply {

            visibility = View.VISIBLE
        }

        mRecordsRecyclerView = view.recordsRecyclerView.apply {

            addOnScrollListener(RealtimeScrollListener(::getRecords))
        }

        setupRecyclerView()

        mCreateNewActivityView = view.createNewActivityTextView

        recordsListViewModel =
            ViewModelProviders.of(this).get(RecordsListViewModel::class.java)

        getRecords()

        // Inflate the layout for this fragment
        return view
    }

    private fun getRecords() {
        val recordsListLiveData = recordsListViewModel?.getRecordsListLiveData() ?: return

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
                    mLoader.visibility = View.GONE
                }
            }
        }
    }

    private fun addRecord(record: TimeoRecord, id: String) {
        mRecords.add(record)
        mItemIds.add(id)
        mViewAdapter.notifyItemInserted(mRecords.size - 1)
    }

    private fun removeRecord(id: String) {

        val index = mRecords.withIndex().filterIndexed { index, _ -> mItemIds[index] == id }
            .map { it.index }
            .first()

        mRecords.removeAt(index)
        mItemIds.remove(id)

        mViewAdapter.notifyItemRemoved(index)
    }

    private fun modifyRecord(record: TimeoRecord, id: String) {
        val index =
            mRecords.withIndex().filterIndexed { index, _ -> mItemIds[index] == id }
                .map { it.index }
                .first()

        mRecords[index] = record

        mViewAdapter.notifyItemChanged(index)
    }

    private fun setupRecyclerView() {

        val viewManager = LinearLayoutManager(context)

        mViewAdapter = RecordsListAdapter(
            mRecords,
            ::showDialog
        )

        mRecordsRecyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter
        }
    }

    private fun showDialog(itemIndex: Int) {

        val dialog = AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle("Are you sure?")
            .setMessage("Are you sure you want to delete this record?")
            .setPositiveButton("Yes", this)
            .setNegativeButton("No", null)

        chosenRecordIndex = itemIndex

        dialog.show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

        if (chosenRecordIndex == -1) {

            return
        }
        val recordTime = mRecords[chosenRecordIndex].workingTime.toLong()

        recordsListViewModel?.deleteRecord(mItemIds[chosenRecordIndex], recordTime, mRecords[chosenRecordIndex].activityId)


//        activity.update("totalTime", FieldValue.increment(-workingTime))

        Snackbar.make(view!!, "Record deleted", Snackbar.LENGTH_LONG).show()
    }
}
