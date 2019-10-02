package com.jdevs.timeo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KProperty

class AddActivityFragment : DataManipulatorFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        Records.writeFile()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_activity, container, false)
    }
}
