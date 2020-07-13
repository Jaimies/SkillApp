package com.jdevs.timeo.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.AddTaskLayoutBinding
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.hardware.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_task_layout.name_edit_text

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private val viewModel: AddTaskViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.projectId = requireArguments().getString("projectId")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddTaskLayoutBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.alpha = 0f
        view.animate().alpha(1f)
        name_edit_text.requestFocus()
        name_edit_text.showKeyboard()

        requireActivity().onBackPressedDispatcher.addCallback(this) { destroy() }
        observe(viewModel.dismiss) { destroy() }
    }

    private fun destroy() {

        parentFragmentManager.commit { remove(this@AddTaskFragment) }
        hideKeyboard()
    }

    companion object {

        fun create(fragmentManager: FragmentManager, projectId: String) {

            fragmentManager.commit {
                add<AddTaskFragment>(R.id.add_task_frag, args = bundleOf("projectId" to projectId))
            }
        }
    }
}
