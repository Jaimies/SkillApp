package com.jdevs.timeo.ui.common.adapter

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem
import java.util.UUID
import kotlin.reflect.KClass

interface DelegateAdapter {

    fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit = { _, _ -> },
        navigateToDetails: (Int) -> Unit = {},
        showDeleteDialog: (Int) -> Unit = {}
    ): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem)
}

fun <T : ViewModel> createViewModel(fragmentActivity: FragmentActivity, modelClass: KClass<T>) =
    ViewModelProvider(fragmentActivity).get(UUID.randomUUID().toString(), modelClass.java)
