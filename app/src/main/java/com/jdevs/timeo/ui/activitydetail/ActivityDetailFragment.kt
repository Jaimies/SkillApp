package com.jdevs.timeo.ui.activitydetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.OverviewDirections
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ActivitydetailFragBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.Recordable
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.ui.common.adapter.ListAdapter
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.showTimePicker
import com.jdevs.timeo.util.time.getMins
import com.jdevs.timeo.util.ui.navigateAnimated
import com.jdevs.timeo.util.ui.setupAdapter
import kotlinx.android.synthetic.main.activitydetail_frag.stats_viewpager
import kotlinx.android.synthetic.main.activitydetail_frag.subactivities_recycler_view
import javax.inject.Inject

class ActivityDetailFragment : ActionBarFragment() {
    private val viewModel: ActivityDetailViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (!modelClass.isAssignableFrom(ActivityDetailViewModel::class.java)) {
                    throw IllegalArgumentException("ViewModel not found")
                }

                @Suppress("UNCHECKED_CAST")
                return viewModelFactory.create(args.activityId) as T
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ActivityDetailViewModel.Factory

    override val menuId = R.menu.activitydetail_frag_menu
    private val args: ActivityDetailFragmentArgs by navArgs()
    private val subactivitiesAdapter = ListAdapter(
        SubactivityDelegateAdapter(::showSubactivityRecordDialog, ::navigateToSubActivity)
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ActivitydetailFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        stats_viewpager.adapter =
            ChartsAdapter(viewModel.dayStats, viewModel.weekStats, viewModel.monthStats)

        subactivities_recycler_view.setupAdapter(subactivitiesAdapter)

        observe(viewModel.activity) { activity ->
            viewModel.setActivity(activity)
            subactivitiesAdapter.submitList(activity.subActivities)
        }
        observe(viewModel.showRecordDialog) { showRecordDialog() }
        observe(viewModel.showParentRecordDialog) { showRecordDialog { activity -> activity.parentActivity } }
        observe(viewModel.navigateToParentActivity) {
            val directions = OverviewDirections
                .actionToActivityDetailFragment(viewModel.activity.value!!.parentActivity!!.id)
            findNavController().navigateAnimated(directions)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.editActivity) {
            val directions = OverviewDirections
                .actionToAddActivityFragment(viewModel.activity.value!!)

            findNavController().navigateAnimated(directions)
            return true
        }

        return false
    }

    private fun showSubactivityRecordDialog(index: Int) {
        showRecordDialog { activity -> activity.subActivities[index] }
    }

    private fun navigateToSubActivity(index: Int) {
        val directions = OverviewDirections
            .actionToActivityDetailFragment(viewModel.activity.value!!.subActivities[index].id)

        findNavController().navigateAnimated(directions)
    }

    private fun showRecordDialog(getActivity: (activity: ActivityItem) -> Recordable? = { it }) {
        showTimePicker { hours, minutes ->
            val activity = getActivity(viewModel.activity.value!!) ?: return@showTimePicker
            viewModel.addRecord(activity.id, activity.name, getMins(hours, minutes))
        }
    }
}
