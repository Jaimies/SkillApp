package com.jdevs.timeo.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.jdevs.timeo.databinding.AddTaskLayoutBinding
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.observeEvent
import com.jdevs.timeo.util.showKeyboard
import kotlinx.android.synthetic.main.add_task_layout.name_edit_text
import javax.inject.Inject

class AddTaskFragment : Fragment() {

    @Inject
    lateinit var viewModel: AddTaskViewModel

    private lateinit var onBackPressed: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddTaskLayoutBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        onBackPressed = requireActivity().onBackPressedDispatcher.addCallback(this) { hide() }
        observeEvent(viewModel.dismiss) { hide() }

        return binding.root
    }

    private fun hide() {

        view?.visibility = GONE
        onBackPressed.isEnabled = false
        viewModel.name.value = ""
        hideKeyboard()
    }

    fun show(projectId: String) {

        view?.visibility = VISIBLE
        view?.alpha = 0f
        view?.animate()?.alpha(1f)

        onBackPressed.isEnabled = true
        viewModel.projectId = projectId

        name_edit_text.requestFocus()
        name_edit_text.showKeyboard()
    }
}
