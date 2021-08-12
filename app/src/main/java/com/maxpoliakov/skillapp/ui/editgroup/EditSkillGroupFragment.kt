package com.maxpoliakov.skillapp.ui.editgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.maxpoliakov.skillapp.databinding.EditableLayoutBinding
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditSkillGroupFragment : Fragment() {
    private val viewModel by viewModels { viewModelFactory.create(args.group) }

    private val args: EditSkillGroupFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: EditSkillGroupViewModel.Factory

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
}
