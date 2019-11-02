package com.jdevs.timeo


import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.helpers.TimeHelper
import com.jdevs.timeo.models.ActionBarFragment
import kotlinx.android.synthetic.main.fragment_activity_details.view.*
import java.util.*
import java.util.concurrent.TimeUnit


class ActivityDetailsFragment : ActionBarFragment() {

    private val args: ActivityDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_activity_details, container, false)

        view.mainTextView.text = args.timeoActivity.title


        view.totalTimeTextView.apply {

            val totalHours = TimeHelper.minsToHours(args.timeoActivity.totalTime)

            text = "${totalHours}h"

        }

        view.averageTimeTextView.apply {

            val msDiff = Calendar.getInstance().timeInMillis - args.timeoActivity.timestamp.time
            val daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff).toInt()


            val days = if (daysDiff != 0) daysDiff else 1


            val avgTime = args.timeoActivity.totalTime / days / 60.0f

            val avgTimeString = "%.1f".format(avgTime)


            val result =
                if (avgTimeString.takeLast(1) == "0") avgTimeString.dropLast(2) else avgTimeString


            text = "${result}h"

        }

        // Inflate the layout for this fragment
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_activity_details)

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
