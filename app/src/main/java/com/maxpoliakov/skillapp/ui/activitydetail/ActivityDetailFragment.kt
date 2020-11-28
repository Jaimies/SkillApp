package com.maxpoliakov.skillapp.ui.activitydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.charts.LineChart
import com.maxpoliakov.skillapp.OverviewDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.ActivitydetailFragBinding
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment
import com.maxpoliakov.skillapp.util.charts.setup
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activitydetail_frag.chart
import javax.inject.Inject

@AndroidEntryPoint
class ActivityDetailFragment : ActionBarFragment(R.menu.activitydetail_frag_menu) {
    private val viewModel by viewModels { viewModelFactory.create(args.activityId) }

    @Inject
    lateinit var viewModelFactory: ActivityDetailViewModel.Factory

    private val args: ActivityDetailFragmentArgs by navArgs()

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
        val chart = chart as LineChart
        chart.setup()
        observe(viewModel.stats, chart::setState)
        observe(viewModel.showRecordDialog) { showRecordDialog() }
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

    private fun showRecordDialog() {
        showTimePicker { duration ->
            val activity = viewModel.activity.value!!
            viewModel.addRecord(activity.id, activity.name, duration)
        }
    }
}
