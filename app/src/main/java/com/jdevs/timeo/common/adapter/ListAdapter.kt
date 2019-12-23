package com.jdevs.timeo.common.adapter

import android.content.Context
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.ui.activities.adapter.ActivityDelegateAdapter
import com.jdevs.timeo.ui.activities.adapter.RecordDelegateAdapter
import com.jdevs.timeo.util.AdapterConstants.ACTIVITY
import com.jdevs.timeo.util.AdapterConstants.LOADING
import com.jdevs.timeo.util.AdapterConstants.RECORD

@Suppress("TooManyFunctions")
abstract class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataItemCount get() = items.filter { it.getViewType() != LOADING }.size

    protected val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()
    private val items = mutableListOf<ViewType>()
    private val ids = mutableListOf<String>()
    private var isLastItemReached = false

    private val loadingItem = object : ViewType {

        override var id = -1
        override fun getViewType() = LOADING
    }

    init {

        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ACTIVITY, ActivityDelegateAdapter())
        delegateAdapters.put(RECORD, RecordDelegateAdapter())
        items.add(loadingItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()
    override fun getItemCount() = items.size
    fun getItem(index: Int) = items[index]
    fun getId(index: Int): String = ids[index]

    fun addItem(item: ViewType, id: String) = items.apply {

        if (ids.contains(id)) {

            modifyItem(item, id)
            return@apply
        }

        hideLoader()
        add(item)
        ids.add(id)

        notifyItemInserted(lastIndex)
    }

    fun modifyItem(item: ViewType, id: String) {

        val index = ids.indexOf(id)

        if (items[index] != item) {

            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun removeItem(id: String) {

        val index = ids.indexOf(id)

        items.removeAt(index)
        ids.removeAt(index)

        notifyItemRemoved(index)
    }

    fun onLastItemReached() {

        isLastItemReached = true
        hideLoader()
    }

    fun showLoader() {

        if (!isLastItemReached && !items.contains(loadingItem)) {

            items.add(loadingItem)
            notifyItemInserted(items.lastIndex)
        }
    }

    fun setItems(newItems: List<ViewType>) {

        val diffResult = DiffUtil.calculateDiff(DiffCallback(newItems, items))

        items.clear()
        items.addAll(newItems)

        diffResult.dispatchUpdatesTo(this)
    }

    private fun hideLoader() {

        val index = items.indexOf(loadingItem)

        if (index != -1) {

            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class DiffCallback(
        private var newItems: List<ViewType>,
        private var oldItems: List<ViewType>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {

            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {

            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        protected val context: Context = view.context
        protected val lifecycleOwner = context as LifecycleOwner
    }
}
