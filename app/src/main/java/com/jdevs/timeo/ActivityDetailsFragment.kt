package com.jdevs.timeo


import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.models.ActionBarFragment
import kotlinx.android.synthetic.main.fragment_activity_details.view.*


class ActivityDetailsFragment : ActionBarFragment() {

    private val args : ActivityDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_activity_details, container, false)

        view.mainTextView.apply {

            text = args.activityTitle

        }

        // Inflate the layout for this fragment
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_activity_details)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if(item.itemId == R.id.editActivity) {

            val directions = ActivityDetailsFragmentDirections.actionEditActivity(true, args.activityId, args.activityTitle, args.activityIcon)

            findNavController().navigate(directions)

            true


        } else {

            super.onOptionsItemSelected(item)

        }
    }

}
