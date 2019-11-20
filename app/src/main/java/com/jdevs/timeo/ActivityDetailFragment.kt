package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.databinding.FragmentActivityDetailBinding
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.states.ActivityDetailsState
import com.jdevs.timeo.util.getHoursSinceDate
import com.jdevs.timeo.util.minsToHours

class ActivityDetailFragment : ActionBarFragment() {

    private val args: ActivityDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentActivityDetailBinding.inflate(inflater, container, false)

        val totalTime = minsToHours(args.timeoActivity.totalTime) + "h"
        val daysCount = getHoursSinceDate(args.timeoActivity.timestamp)

        val avgDailyMins = args.timeoActivity.totalTime / (daysCount + 1)
        val avgDailyTime = minsToHours(avgDailyMins) + "h"

        val lastWeekTime = "42h"

        val state =
            ActivityDetailsState(args.timeoActivity.name, totalTime, avgDailyTime, lastWeekTime)

        binding.lifecycleOwner = this
        binding.state = state

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        createOptionsMenu(menu, inflater, R.menu.action_bar_activity_details)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.editActivity) {
            val directions = ActivityDetailFragmentDirections.actionEditActivity(
                true,
                args.activityId,
                args.timeoActivity
            )

            findNavController().navigate(directions)

            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }
}
