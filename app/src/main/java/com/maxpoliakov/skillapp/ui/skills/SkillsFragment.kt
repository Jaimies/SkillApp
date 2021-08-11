package com.maxpoliakov.skillapp.ui.skills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.MainDirections.Companion.actionToSkillDetailFragment
import com.maxpoliakov.skillapp.R.id.addskill_fragment_dest
import com.maxpoliakov.skillapp.databinding.SkillsFragBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.ItemTouchHelperCallback
import com.maxpoliakov.skillapp.util.ui.createDraggingItemTouchHelper
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setOnItemAddedListener
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SkillsFragment : Fragment() {
    private val itemTouchHelperCallback = object : ItemTouchHelperCallback {
        override fun onMove(from: Int, to: Int) {
            listAdapter.moveItem(from, to)
        }

        override fun onDropped() {
            viewModel.updateOrder(listAdapter.currentList.filterIsInstance<Skill>())
        }

        override fun onGroup(first: Skill, second: Skill) {
            viewModel.createGroup("New group", listOf(first, second))
        }

        override fun onGroup(skill: Skill, skillGroup: SkillGroup) {
            viewModel.addToGroup(skill, skillGroup)
        }
    }

    private val itemTouchHelper by lazy {
        createDraggingItemTouchHelper(requireContext(), itemTouchHelperCallback)
    }

    private lateinit var binding: SkillsFragBinding

    @Inject
    lateinit var listAdapterFactory: SkillListAdapter.Factory

    private val listAdapter by lazy {
        listAdapterFactory.create(this::startDrag).apply {
            setOnItemAddedListener {
                binding.recyclerView.smoothScrollToPosition(0)
            }
        }
    }
    private val viewModel: SkillsViewModel by viewModels()

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
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        lifecycleScope.launch {
            viewModel.skillsAndGroups.collect {
                listAdapter.submitList(it.skills + it.groups)
            }
        }

        observe(viewModel.isActive, this::setStopwatchActive)

        observe(viewModel.navigateToAddEdit) {
            findNavController().navigateAnimated(addskill_fragment_dest)
        }
    }

    private fun setStopwatchActive(isActive: Boolean) {
        if (isActive) showStopwatch()
        else hideStopwatch()
    }

    private fun showStopwatch() {
        listAdapter.showStopwatch()
        binding.recyclerView.smoothScrollToPosition(0)
    }

    private fun hideStopwatch() = listAdapter.hideStopwatch()

    private fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}
