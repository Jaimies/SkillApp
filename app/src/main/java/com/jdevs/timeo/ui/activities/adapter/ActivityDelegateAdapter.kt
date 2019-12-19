package com.jdevs.timeo.ui.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.adapter.ViewTypeDelegateAdapter
import com.jdevs.timeo.data.Task
import com.jdevs.timeo.databinding.ActivitiesItemBinding
import com.jdevs.timeo.ui.activities.RecordDialog
import com.jdevs.timeo.ui.activities.viewmodel.ActivityViewModel
import com.jdevs.timeo.util.randomString

class ActivityDelegateAdapter :
    ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        goToDetails: (Int) -> Unit,
        deleteRecord: (Int) -> Unit
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = ActivitiesItemBinding.inflate(inflater, parent, false).also {

            it.viewmodel = ViewModelProviders
                .of(parent.context as FragmentActivity)
                .get(randomString(), ActivityViewModel::class.java)

            it.lifecycleOwner = parent.context as FragmentActivity
        }

        return ViewHolder(binding, createRecord, goToDetails)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {

        holder as ViewHolder
        holder.bindActivity(item as Task)
    }

    class ViewHolder(
        private val binding: ActivitiesItemBinding,
        private val createRecord: (Int, Long) -> Unit = { _, _ -> },
        private val navigateToDetails: (Int) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root),
        ActivityViewModel.Navigator {

        init {

            binding.viewmodel?.navigator = this
        }

        fun bindActivity(activity: Task) = binding.viewmodel?.setActivity(activity)

        override fun showRecordDialog() =
            RecordDialog(binding.root.context, adapterPosition, createRecord).show()

        override fun navigateToDetails() = navigateToDetails(adapterPosition)
    }
}
