package com.maxpoliakov.skillapp.shared.recyclerview.adapter

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class ListAdapter<T, VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH> {
    private val mDiffer: AsyncListDiffer<T>
    private val mListener =
        ListListener<T> { previousList, currentList -> onCurrentListChanged(previousList, currentList) }

    protected constructor(diffCallback: DiffUtil.ItemCallback<T>) {
        mDiffer = AsyncListDiffer(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder(diffCallback).build()
        ).apply {
            addListListener(mListener)
        }
    }

    protected constructor(config: AsyncDifferConfig<T>) {
        mDiffer = AsyncListDiffer(AdapterListUpdateCallback(this), config).apply {
            addListListener(mListener)
        }
    }

    fun setListWithoutDiffing(list: List<T>) {
        setOf("mList", "mReadOnlyList").forEach { fieldName ->
            val field = mDiffer::class.java.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(mDiffer, list)
        }
    }

    fun addListListener(listener: ListListener<T>) {
        mDiffer.addListListener(listener)
    }

    open fun submitList(list: List<T>?) {
        mDiffer.submitList(list)
    }

    fun submitList(list: List<T>?, commitCallback: Runnable?) {
        mDiffer.submitList(list, commitCallback)
    }

    fun getItem(position: Int): T {
        return mDiffer.currentList[position]
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    val currentList: List<T>
        get() = mDiffer.currentList

    open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {}
}
