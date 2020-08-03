package com.jdevs.timeo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.OverviewDirections.Companion.actionToActivityDetailFragment
import com.jdevs.timeo.R.id.addactivity_fragment_dest
import com.jdevs.timeo.R.menu.activities_frag_menu
import com.jdevs.timeo.databinding.ActivitiesFragBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.showTimePicker
import com.jdevs.timeo.util.time.getMins
import com.jdevs.timeo.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.history_frag.recycler_view

private const val VISIBLE_THRESHOLD = 5

@AndroidEntryPoint
class ActivitiesFragment : ListFragment<ActivityItem>(activities_frag_menu) {

    override val delegateAdapter by lazy {
        ActivityDelegateAdapter(::showRecordDialog, ::navigateToDetails)
    }

    override val viewModel: ActivitiesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding =
            ActivitiesFragBinding.inflate(inflater, container, false).also {
                it.lifecycleOwner = viewLifecycleOwner
                it.viewModel = viewModel
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setup(VISIBLE_THRESHOLD)

        observe(viewModel.navigateToAddEdit) {
            findNavController().navigateAnimated(addactivity_fragment_dest)
        }
    }

    private fun navigateToDetails(index: Int) {
        val activity = getItem(index)
        val directions = actionToActivityDetailFragment(activity.id)
        findNavController().navigateAnimated(directions)
    }

    private fun showRecordDialog(index: Int) {
        showTimePicker { hours, minutes ->
            val activity = getItem(index)
            val totalMins = getMins(hours, minutes)
            viewModel.createRecord(activity, totalMins)
        }
    }
}
