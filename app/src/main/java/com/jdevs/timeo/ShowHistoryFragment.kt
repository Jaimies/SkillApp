package com.jdevs.timeo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController


class ShowHistoryFragment : FragmentWithActionBarNavigation() {
    private lateinit var records : Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_history, container, false)

        records = Data(context)
        records.addRecordsToView(R.id.historyContainer, view)

        // Inflate the layout for this fragment
        return view
    }


}