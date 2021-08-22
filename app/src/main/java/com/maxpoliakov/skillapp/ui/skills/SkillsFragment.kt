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
import com.maxpoliakov.skillapp.R.id.addskill_fragment_dest
import com.maxpoliakov.skillapp.databinding.SkillsFragBinding
import com.maxpoliakov.skillapp.domain.model.Orderable
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.CardViewDecoration
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupViewHolder
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.Change
import com.maxpoliakov.skillapp.util.ui.ItemTouchHelperCallback
import com.maxpoliakov.skillapp.util.ui.createDraggingItemTouchHelper
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SkillsFragment : Fragment() {
    private var lastItemDropTime = 0L

    private val itemTouchHelperCallback = object : ItemTouchHelperCallback {
        override fun onMove(from: Int, to: Int) {
            listAdapter.moveItem(from, to)
        }

        override fun onLeaveGroup(skill: Skill) {
            lastItemDropTime = System.nanoTime()
            viewModel.removeFromGroup(skill)
            updateOldGroupIfNeeded(skill)
            updateSkillGroup(skill, -1)
        }

        override fun onDropped(change: Change?) {
            val list = listAdapter.currentList.filterIsInstance<Orderable>()
            lastItemDropTime = System.nanoTime()

            when (change) {
                is Change.CreateGroup -> {
                    val group = SkillGroup(
                        id = 0,
                        name = "New Group",
                        skills = listOf(change.skill, change.otherSkill),
                        order = change.position,
                    )

                    val createGroupAsync = viewModel.createGroupAsync(change.skill, group)

                    createGroupAsync.invokeOnCompletion {
                        val groupId = createGroupAsync.getCompleted().toInt()
                        updateSkillGroup(change.skill, groupId)
                        updateSkillGroup(change.otherSkill, groupId)

                        val newGroup = group.copy(id = groupId)

                        listAdapter.addItem(change.position + 1, newGroup)
                        listAdapter.addItem(change.position + group.skills.size + 2, SkillGroupFooter(groupId))
                    }
                }
                is Change.AddToGroup -> {
                    viewModel.addToGroup(change.skill, change.groupId)

                    updateOldGroupIfNeeded(change.skill)
                    addSkillToGroup(change)
                    updateSkillGroup(change.skill, change.groupId)
                }
            }

            viewModel.updateOrder(getUpdatedList(list, change))
        }

        private fun addSkillToGroup(change: Change.AddToGroup) {
            val groupViewHolder = findGroupViewHolderById(change.groupId)

            if (groupViewHolder != null) {
                val group = groupViewHolder.viewModel.skillGroup.value!!
                groupViewHolder.setSkillGroup(group.copy(skills = group.skills + change.skill))
            }
        }

        private fun updateSkillGroup(skill: Skill, newGroupId: Int) {
            val skillViewHolder = findSkillViewHolderById(skill.id)

            if (skillViewHolder != null) {
                val updatedSkill = skill.copy(groupId = newGroupId)
                val position = skillViewHolder.absoluteAdapterPosition

                skillViewHolder.setItem(updatedSkill)
                listAdapter.updateSilently(position, updatedSkill)
            }
        }

        private fun updateOldGroupIfNeeded(skill: Skill) {
            if (skill.groupId == -1) return

            val groupViewHolder = findGroupViewHolderById(skill.groupId)

            if (groupViewHolder != null) {
                val group = groupViewHolder.viewModel.skillGroup.value!!

                if (group.skills.size <= 1) {
                    val position = groupViewHolder.absoluteAdapterPosition
                    val footer = findGroupFooterViewHolderById(group.id)

                    val updatedList = listAdapter.currentList.toMutableList().apply {
                        if (footer != null) removeAt(footer.absoluteAdapterPosition)
                        removeAt(position)
                    }

                    listAdapter.setListWithoutDiffing(updatedList)

                    if (footer != null)
                        listAdapter.notifyItemRemoved(footer.absoluteAdapterPosition)

                    listAdapter.notifyItemRemoved(position)
                } else {
                    removeSkillFromGroup(groupViewHolder, group, skill)
                }
            }
        }

        private fun removeSkillFromGroup(
            groupViewHolder: SkillGroupViewHolder,
            group: SkillGroup,
            skill: Skill
        ) {
            groupViewHolder.setSkillGroup(
                group.copy(skills = group.skills.filter { it.id != skill.id })
            )
        }

        private inline fun <reified T : RecyclerView.ViewHolder> findViewHolder(predicate: (T) -> Boolean): T? {
            for (i in 0 until listAdapter.itemCount) {
                val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(i)
                if (viewHolder is T && predicate(viewHolder)) return viewHolder
            }

            return null
        }

        private fun findGroupViewHolderById(groupId: Int): SkillGroupViewHolder? {
            return findViewHolder { viewHolder ->
                viewHolder.viewModel.skillGroup.value!!.id == groupId
            }
        }

        private fun findGroupFooterViewHolderById(groupId: Int): SkillGroupFooterViewHolder? {
            return findViewHolder { viewHolder ->
                viewHolder.groupId == groupId
            }
        }

        private fun findSkillViewHolderById(skillId: Int): SkillViewHolder? {
            return findViewHolder { viewHolder ->
                viewHolder.viewModel.skill.value!!.id == skillId
            }
        }

        private fun getUpdatedList(list: List<Orderable>, change: Change?): List<Orderable> {
            if (change is Change.AddToGroup)
                return list.withSkillGroupId(change.skill, change.groupId)

            return list
        }

        private fun List<Orderable>.withSkillGroupId(skill: Skill, groupId: Int): List<Orderable> {
            val updatedSkill = skill.copy(groupId = groupId)

            return map { item ->
                if (item is Skill && item.id == updatedSkill.id) updatedSkill
                else item
            }
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
        binding.recyclerView.addItemDecoration(
            CardViewDecoration(requireContext(), 16.dp.toPx(requireContext()).toFloat())
        )
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        lifecycleScope.launch {
            viewModel.skillsAndGroups.collect {
                // Don't update within 0.5sec after the drag and drop has finished, as those updates cause awful transitions
                if (System.nanoTime() - lastItemDropTime < 500_000_000)
                    return@collect

                val list = (it.skills + it.groups)
                    .sortedBy { it.order }
                    .flatMap { item ->
                        when (item) {
                            is Skill -> listOf(item)
                            is SkillGroup -> listOf(item) + item.skills.sortedBy { it.order } + listOf(SkillGroupFooter(item.id))
                            else -> throw IllegalStateException("Orderables cannot be anything other than Skill or SkillGroup objects")
                        }
                    }

                listAdapter.submitList(list)
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
