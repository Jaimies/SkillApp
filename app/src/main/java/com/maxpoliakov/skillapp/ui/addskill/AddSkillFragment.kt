package com.maxpoliakov.skillapp.ui.addskill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.databinding.AddskillFragBinding
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddSkillFragment : Fragment() {
    private val viewModel by viewModels { viewModelFactory.create() }

    @Inject
    lateinit var viewModelFactory: AddSkillViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AddskillFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(viewModel.navigateBack) { findNavController().popBackStack() }
        observe(viewModel.hideKeyboard) { hideKeyboard() }
    }
}
