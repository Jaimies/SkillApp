package com.maxpoliakov.skillapp.ui.editskill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maxpoliakov.skillapp.databinding.EditableLayoutBinding
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditSkillFragment : Fragment() {
    private val viewModel by viewModels { viewModelFactory.create(args.skill!!) }

    @Inject
    lateinit var viewModelFactory: EditSkillViewModel.Factory
    private val args: EditSkillFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = EditableLayoutBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(viewModel.navigateBack) { findNavController().popBackStack() }
    }
}
