package com.maxpoliakov.skillapp.ui.skills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.MainDirections.Companion.actionToSkillDetailFragment
import com.maxpoliakov.skillapp.R.id.addskill_fragment_dest
import com.maxpoliakov.skillapp.databinding.SkillsFragBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SkillsFragment : Fragment() {
    @Inject
    lateinit var delegateAdapterFactory: SkillDelegateAdapter.Factory

    private lateinit var binding: SkillsFragBinding

    private val delegateAdapter by lazy {
        delegateAdapterFactory.create(this::navigateToDetails)
    }

    private val listAdapter by lazy { SkillListAdapter(delegateAdapter) }
    val viewModel: SkillsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SkillsFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.setupAdapter(listAdapter)
        viewModel.skills.observe(viewLifecycleOwner, listAdapter::submitList)

        observe(viewModel.navigateToAddEdit) {
            findNavController().navigateAnimated(addskill_fragment_dest)
        }

        observe(viewModel.navigateToSkill, this::navigateToDetails)
    }

    private fun navigateToDetails(skill: Skill) {
        val directions = actionToSkillDetailFragment(skill.id)
        findNavController().navigateAnimated(directions)
    }
}
