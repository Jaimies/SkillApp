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
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.utils.Time
import kotlinx.android.synthetic.main.fragment_activity_details.view.averageTimeTextView
import kotlinx.android.synthetic.main.fragment_activity_details.view.lastWeekTextView
import kotlinx.android.synthetic.main.fragment_activity_details.view.mainTextView
import kotlinx.android.synthetic.main.fragment_activity_details.view.totalTimeTextView

class ActivityDetailsFragment : ActionBarFragment() {

    private val args: ActivityDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_activity_details, container, false)

        view.mainTextView.text = args.timeoActivity.title

        view.totalTimeTextView.apply {

            val totalHours = Time.minsToHours(args.timeoActivity.totalTime)

            text = "${totalHours}h"
        }

        view.averageTimeTextView.apply {

            val days = Time.getHoursSinceDate(args.timeoActivity.timestamp)

            val avgMins = args.timeoActivity.totalTime / (days + 1)
            val avgHours = Time.minsToHours(avgMins)

            text = "${avgHours}h"
        }

        view.lastWeekTextView.apply {

            text = "42h"
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        createOptionsMenu(menu, inflater, R.menu.action_bar_activity_details)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.editActivity) {

            val directions = ActivityDetailsFragmentDirections.actionEditActivity(
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
