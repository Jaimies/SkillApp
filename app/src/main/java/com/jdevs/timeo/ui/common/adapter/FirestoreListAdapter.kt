package com.jdevs.timeo.ui.common.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.ui.activities.ActivityDelegateAdapter
import com.jdevs.timeo.ui.history.RecordDelegateAdapter
import com.jdevs.timeo.ui.projects.ProjectDelegateAdapter
import com.jdevs.timeo.ui.stats.StatisticDelegateAdapter
import com.jdevs.timeo.util.ViewTypes.ACTIVITY
import com.jdevs.timeo.util.ViewTypes.LOADING
import com.jdevs.timeo.util.ViewTypes.PROJECT
import com.jdevs.timeo.util.ViewTypes.RECORD
import com.jdevs.timeo.util.ViewTypes.STATISTIC

class FirestoreListAdapter(
    private val createRecord: (Int, Long) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {},
    private val showDeleteDialog: (Int) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataItemCount get() = items.filter { it.viewType != LOADING }.size

    private val delegateAdapters = SparseArray<DelegateAdapter>()
    private val items = mutableListOf<ViewItem>()

    private val loadingItem = object : ViewItem {

        override var id = -1
        override var documentId = ""
        override val viewType = LOADING
    }

    init {

        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ACTIVITY, ActivityDelegateAdapter())
        delegateAdapters.put(PROJECT, ProjectDelegateAdapter())
        delegateAdapters.put(RECORD, RecordDelegateAdapter())
        delegateAdapters.put(STATISTIC, StatisticDelegateAdapter())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType)
            .onCreateViewHolder(parent, createRecord, navigateToDetails, showDeleteDialog)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items[position].viewType

    fun getItem(position: Int) = items[position]

    fun showLoader() {

        hideLoader()

        items.add(loadingItem)
        notifyItemInserted(items.lastIndex)
    }

    fun hideLoader() {

        val index = items.indexOf(loadingItem)

        if (index != -1) {

            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun addItem(item: ViewItem) {

        if (items.count { it.documentId == item.documentId } > 0) {

            modifyItem(item)
            return
        }

        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    fun modifyItem(item: ViewItem) {

        val index = items.indexOfFirst { it.documentId == item.documentId }

        if (items[index] != item) {

            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun removeItem(item: ViewItem) {

        val index = items.indexOfFirst { it.documentId == item.documentId }

        runCatching {

            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
