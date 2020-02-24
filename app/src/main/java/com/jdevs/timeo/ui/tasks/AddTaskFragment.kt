package com.jdevs.timeo.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import com.jdevs.timeo.R
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

        requireActivity().onBackPressedDispatcher.addCallback(this) { destroy() }
        observeEvent(viewModel.dismiss) { destroy() }

        viewModel.projectId = requireArguments().getString("projectId")!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.alpha = 0f
        view.animate().alpha(1f)
        name_edit_text.requestFocus()
        name_edit_text.showKeyboard()
    }

    private fun destroy() {

        parentFragmentManager.beginTransaction().remove(this).commit()
        hideKeyboard()
    }

    companion object {

        fun create(fragmentManager: FragmentManager, projectId: String) {

            val args = Bundle()
            args.putString("projectId", projectId)

            fragmentManager.beginTransaction()
                .add<AddTaskFragment>(R.id.add_task_frag, args = args)
                .commit()
        }
    }
}
