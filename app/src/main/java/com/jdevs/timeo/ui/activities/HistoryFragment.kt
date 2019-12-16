package com.jdevs.timeo.ui.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.R
import com.jdevs.timeo.common.InfiniteScrollListener
import com.jdevs.timeo.common.ItemListFragment
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.HistoryFragBinding
import com.jdevs.timeo.ui.activities.adapter.RecordsAdapter
import com.jdevs.timeo.ui.activities.viewmodel.HistoryViewModel
import com.jdevs.timeo.util.RecordsConstants

class HistoryFragment : ItemListFragment<Record>(),
    DialogInterface.OnClickListener,
    HistoryViewModel.Navigator {

    override val menuId = R.menu.history_fragment_menu
    override val mAdapter by lazy {
        RecordsAdapter(
            ::showDeleteDialog
        )
    }

    override val viewModel by lazy {
        ViewModelProviders.of(this).get(HistoryViewModel::class.java).also {
            it.navigator = this
        }
    }

    private var chosenItemIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = HistoryFragBinding.inflate(inflater, container, false).also {

            it.viewmodel = viewModel
            it.lifecycleOwner = this

            it.recyclerView.apply {

                linearLayoutManager = LinearLayoutManager(context)

                layoutManager = linearLayoutManager
                adapter = mAdapter

                addOnScrollListener(
                    InfiniteScrollListener(
                        ::getItems,
                        linearLayoutManager,
                        RecordsConstants.VISIBLE_THRESHOLD
                    )
                )
            }
        }

        return binding.root
    }

    private fun showDeleteDialog(index: Int) {

        val dialog = AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.sure_delete_record))
            .setPositiveButton(getString(R.string.yes), this)
            .setNegativeButton(getString(R.string.no), null)

        chosenItemIndex = index

        dialog.show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

        if (chosenItemIndex == -1) {

            return
        }

        val record = getRecord(chosenItemIndex)
        val recordTime = record.time

        Snackbar.make(view!!, getString(R.string.record_deleted), Snackbar.LENGTH_SHORT).show()

        viewModel.deleteRecord(mAdapter.getId(chosenItemIndex), recordTime, record.activityId)
    }

    override fun getItems() {

        observe(viewModel.recordsLiveData)
    }

    private fun getRecord(index: Int) = mAdapter.getItem(index) as Record
}
