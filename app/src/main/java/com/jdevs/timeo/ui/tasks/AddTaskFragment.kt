package com.jdevs.timeo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.showKeyboard
import kotlinx.android.synthetic.main.add_task_layout.name_edit_text

class AddTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.add_task_layout, container, false)

        view.setOnClickListener { hide() }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { hide() }

        return view
    }

    private fun hide() {

        view?.visibility = GONE
        hideKeyboard()
    }

    fun show() {

        view?.visibility = VISIBLE
        view?.alpha = 0f
        view?.animate()?.alpha(1f)

        name_edit_text.requestFocus()
        name_edit_text.showKeyboard()
    }
}
