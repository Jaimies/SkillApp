package com.jdevs.timeo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.models.AuthFragment
import kotlinx.android.synthetic.main.fragment_activity_details.view.*


class ActivityDetailsFragment : AuthFragment() {

    private val args : ActivityDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_activity_details, container, false)

        view.mainTextView.apply {

            text = args.title

        }

        // Inflate the layout for this fragment
        return view
    }


}
