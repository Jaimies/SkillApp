package com.jdevs.timeo.ui.common.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.model.ViewType.ACTIVITY
import com.jdevs.timeo.model.ViewType.LOADING
import com.jdevs.timeo.model.ViewType.PROJECT
import com.jdevs.timeo.model.ViewType.RECORD
import com.jdevs.timeo.model.ViewType.TASK
import com.jdevs.timeo.ui.activities.ActivityDelegateAdapter
import com.jdevs.timeo.ui.history.RecordDelegateAdapter
import com.jdevs.timeo.ui.projects.ProjectDelegateAdapter
import com.jdevs.timeo.ui.tasks.TaskDelegateAdapter

class FirestoreListAdapter(
    createRecord: (Int, Int) -> Unit = { _, _ -> },
    navigateToDetails: (Int) -> Unit = {},
    showDeleteDialog: (Int) -> Unit = {},
    setTaskCompleted: (position: Int, isCompleted: Boolean) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataItemCount get() = items.filter { it.viewType != LOADING }.size

    private val delegateAdapters = SparseArray<DelegateAdapter>()
    private val items = mutableListOf<ViewItem>()

    private val loadingItem = object : ViewItem {

        override val id = ""
        override val viewType = LOADING
    }

    init {

        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ACTIVITY, ActivityDelegateAdapter(createRecord, navigateToDetails))
        delegateAdapters.put(PROJECT, ProjectDelegateAdapter(createRecord, navigateToDetails))
        delegateAdapters.put(RECORD, RecordDelegateAdapter(showDeleteDialog))
        delegateAdapters.put(TASK, TaskDelegateAdapter(setTaskCompleted))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
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

        if (items.count { it.id == item.id } > 0) {

            modifyItem(item)
            return
        }

        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    fun modifyItem(item: ViewItem) {

        val index = items.indexOfFirst { it.id == item.id }

        if (items[index] != item) {

            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun removeItem(item: ViewItem) {

        val index = items.indexOfFirst { it.id == item.id }

        runCatching {

            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
