package com.jdevs.timeo.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.OverviewDirections
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.OverviewFragBinding
import com.jdevs.timeo.ui.activities.ActivityDelegateAdapter
import com.jdevs.timeo.ui.common.adapter.ListAdapter
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.showTimePicker
import com.jdevs.timeo.util.time.getMins
import com.jdevs.timeo.util.ui.navigateAnimated
import com.jdevs.timeo.util.ui.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.overview_frag.activities_list

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val activitiesAdapter by lazy {
        ListAdapter(ActivityDelegateAdapter(::showActivityRecordDialog, ::navigateToActivityDetail))
    }

    private val viewModel: OverviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = OverviewFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.activities.observe(
            R.id.activities_fragment_dest,
            R.id.addactivity_fragment_dest,
            activitiesAdapter, activities_list
        )
    }

    private fun showActivityRecordDialog(index: Int) {
        showTimePicker { hours, minutes -> viewModel.createRecord(index, getMins(hours, minutes)) }
    }

    private fun navigateToActivityDetail(index: Int) {
        val directions = OverviewDirections
            .actionToActivityDetailFragment(viewModel.activities.data.value!![index].id)

        findNavController().navigateAnimated(directions)
    }

    private fun OverviewViewModel.DataWrapper<*>.observe(
        @IdRes listId: Int, @IdRes addId: Int, adapter: ListAdapter, recyclerView: RecyclerView
    ) {
        recyclerView.setupAdapter(adapter)
        observe(data, adapter::submitList)
        observe(navigateToList) { findNavController().navigateAnimated(listId) }
        observe(navigateToAdd) { findNavController().navigateAnimated(addId) }
    }
}
