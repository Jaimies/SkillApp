package com.maxpoliakov.skillapp.ui.skills

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.Hold
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillsFragBinding
import com.maxpoliakov.skillapp.domain.model.Orderable
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.model.Intro
import com.maxpoliakov.skillapp.ui.MainActivity
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment
import com.maxpoliakov.skillapp.ui.common.CardViewDecoration
import com.maxpoliakov.skillapp.ui.intro.IntroUtil
import com.maxpoliakov.skillapp.ui.intro.Intro_3_1_0
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupViewHolder
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.Change
import com.maxpoliakov.skillapp.util.ui.ItemTouchHelperCallback
import com.maxpoliakov.skillapp.util.ui.createReorderAndGroupItemTouchHelper
import com.maxpoliakov.skillapp.util.ui.findViewHolder
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import com.maxpoliakov.skillapp.util.ui.smoothScrollToTop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SkillsFragmentCallback {
    fun navigateToSkillDetail(view: View, skill: Skill)
    fun navigateToGroupDetail(view: View, group: SkillGroup)

    fun startDrag(viewHolder: RecyclerView.ViewHolder)
}

@AndroidEntryPoint
class SkillsFragment : ActionBarFragment<SkillsFragBinding>(R.menu.skills_frag_menu), SkillsFragmentCallback {
    override val layoutId get() = R.layout.skills_frag

    private var lastItemDropTime = 0L

    private val itemTouchHelperCallback = object : ItemTouchHelperCallback {
        override fun onMove(from: Int, to: Int) {
            listAdapter.moveItem(from, to)
        }

        override fun onLeaveGroup(skill: Skill) {
            lastItemDropTime = System.nanoTime()
            viewModel.removeFromGroup(skill)
            updateOldGroupIfNeeded(skill)
            updateGroupIdOfSkill(skill, -1)
        }

        override fun onDropped(change: Change?) {
            val list = listAdapter.currentList.filterIsInstance<Orderable>()
            lastItemDropTime = System.nanoTime()

            when (change) {
                is Change.CreateGroup -> createGroup(change)
                is Change.AddToGroup -> addToGroup(change)
                null -> {}
            }

            viewModel.updateOrder(getUpdatedList(list, change))
        }

        private fun addToGroup(change: Change.AddToGroup) {
            viewModel.addToGroup(change.skill, change.groupId)

            updateOldGroupIfNeeded(change.skill)
            addSkillToGroup(change)
            updateGroupIdOfSkill(change.skill, change.groupId)
        }

        private fun createGroup(change: Change.CreateGroup) {
            val group = SkillGroup(
                id = 0,
                name = getString(R.string.new_group),
                skills = listOf(change.skill, change.otherSkill),
                goal = null,
                order = change.position,
                unit = change.skill.unit,
            )

            val createGroupAsync = viewModel.createGroupAsync(change.skill, group)

            createGroupAsync.invokeOnCompletion {
                val groupId = createGroupAsync.getCompleted().toInt()
                updateGroupIdOfSkill(change.skill, groupId)
                updateGroupIdOfSkill(change.otherSkill, groupId)

                val newGroup = group.copy(id = groupId)

                listAdapter.addItem(change.position + 1, newGroup)
                listAdapter.addItem(change.position + group.skills.size + 2, SkillGroupFooter(newGroup))
            }
        }

        private fun addSkillToGroup(change: Change.AddToGroup) {
            val groupViewHolder = findGroupViewHolderById(change.groupId)

            if (groupViewHolder != null) {
                val group = groupViewHolder.viewModel.skillGroup.value!!
                groupViewHolder.setSkillGroup(group.copy(skills = group.skills + change.skill))
            }
        }

        private fun updateGroupIdOfSkill(skill: Skill, newGroupId: Int) {
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

                if (group.skills.size <= 1)
                    deleteGroup(groupViewHolder, group)
                else
                    removeSkillFromGroup(groupViewHolder, group, skill)
            }
        }

        private fun deleteGroup(
            groupViewHolder: SkillGroupViewHolder,
            group: SkillGroup
        ) {
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

        private fun findGroupViewHolderById(groupId: Int): SkillGroupViewHolder? {
            return requireBinding().recyclerView.findViewHolder { viewHolder ->
                viewHolder.viewModel.skillGroup.value!!.id == groupId
            }
        }

        private fun findGroupFooterViewHolderById(groupId: Int): SkillGroupFooterViewHolder? {
            return requireBinding().recyclerView.findViewHolder { viewHolder ->
                viewHolder.groupId == groupId
            }
        }

        private fun findSkillViewHolderById(skillId: Int): SkillViewHolder? {
            return requireBinding().recyclerView.findViewHolder { viewHolder ->
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

    private var itemTouchHelper: ItemTouchHelper = createReorderAndGroupItemTouchHelper(itemTouchHelperCallback)

    @Inject
    lateinit var listAdapterFactory: SkillListAdapter.Factory

    private val listAdapter by lazy { listAdapterFactory.create(this) }

    @Inject
    lateinit var introUtil: IntroUtil

    @Inject
    lateinit var intro: Intro_3_1_0

    private val viewModel: SkillsViewModel by viewModels()

    private val toolbar get() = (requireActivity() as MainActivity).toolbar

    override fun onBindingCreated(binding: SkillsFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        binding.viewModel = viewModel

        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }

        binding.recyclerView.setupAdapter(listAdapter)
        binding.recyclerView.addItemDecoration(CardViewDecoration())

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        introUtil.showIfNecessary(Intro.Intro_3_1_0) { showIntro() }

        lifecycleScope.launch {
            delay(1)
            viewModel.skillsAndGroups.collect {
                // Don't update within 0.5sec after the drag and drop has finished, as those updates cause awful transitions
                if (System.nanoTime() - lastItemDropTime < 500_000_000)
                    return@collect

                val list = (it.skills + it.groups)
                    .sortedBy { it.order }
                    .flatMap { item ->
                        when (item) {
                            is Skill -> listOf(item)
                            is SkillGroup -> listOf(item) + item.skills.sortedBy { it.order } + listOf(
                                SkillGroupFooter(
                                    item
                                )
                            )
                            else -> throw IllegalStateException("Orderables cannot be anything other than Skill or SkillGroup objects")
                        }
                    }

                if (listAdapter.currentList.run { isNotEmpty() && list.size > this.size }) {
                    whenResumed {
                        requireBinding().recyclerView.smoothScrollToTop()
                    }
                }

                listAdapter.submitList(list)
            }
        }

        observe(viewModel.isActive, this::setStopwatchActive)

        observe(viewModel.navigateToAddSkill) { navigateToAddSkill() }

        observe(viewModel.isEmpty) { isEmpty ->
            editMenuItem?.let { item ->
                if (item.isVisible != !isEmpty) requireActivity().invalidateOptionsMenu()
            }
        }
    }

    override fun onPreDestroyBinding(binding: SkillsFragBinding) {
        binding.recyclerView.adapter = null
        itemTouchHelper.attachToRecyclerView(null)
    }

    private fun showIntro() = lifecycleScope.launch {
        intro.show(toolbar)
    }

    override fun onMenuCreated(menu: Menu) {
        lifecycleScope.launch {
            val isEmpty = viewModel.isEmptyFlow.first()
            editMenuItem?.isVisible = !isEmpty
        }

        updateMenu()
    }

    private val editMenuItem get() = menu?.findItem(R.id.edit)

    override fun onMenuItemSelected(id: Int): Boolean {
        if (id != R.id.edit) return false

        viewModel.toggleEditingMode()
        updateMenu()
        return true
    }

    private fun updateMenu() {
        editMenuItem?.run {
            setIcon(if (viewModel.isInEditingMode) R.drawable.ic_check else R.drawable.ic_edit)
            setTitle(if (viewModel.isInEditingMode) R.string.done else R.string.reorder_skills)
        }
    }

    private fun setStopwatchActive(isActive: Boolean) {
        if (isActive) showStopwatch()
        else hideStopwatch()
    }

    private fun showStopwatch() {
        listAdapter.showStopwatch()
        requireBinding().recyclerView.smoothScrollToTop()
    }

    private fun hideStopwatch() = listAdapter.hideStopwatch()

    override fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        if (!viewModel.isInEditingMode) return

        viewHolder.itemView.translationZ = 10f
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun navigateToSkillDetail(view: View, skill: Skill) {
        val directions = MainDirections.actionToSkillDetailFragment(skill.id)
        navigate(view, directions, R.string.skill_transition_name)
    }

    override fun navigateToGroupDetail(view: View, group: SkillGroup) {
        val directions = MainDirections.actionToSkillGroupFragment(group.id)
        navigate(view, directions, R.string.group_transition_name)
    }

    private fun navigateToAddSkill() {
        val directions = MainDirections.actionToAddSkillFragment()
        navigate(requireBinding().addSkillFab, directions, R.string.add_skill_transition_name)
    }

    private fun navigate(view: View, directions: NavDirections, transitionNameResId: Int) {
        val extras = FragmentNavigatorExtras(view to getString(transitionNameResId))
        navigateWithTransition(directions, extras)
    }

    private fun navigateWithTransition(directions: NavDirections, extras: FragmentNavigator.Extras) {
        setupTransitions()

        findNavController().navigate(directions, extras)

        lifecycleScope.launch {
            delay(resources.getInteger(R.integer.animation_duration).toLong())
            resetTransitions()
        }
    }

    private fun setupTransitions() {
        val transition = Hold().apply {
            duration = resources.getInteger(R.integer.animation_duration).toLong()
        }

        exitTransition = transition
        reenterTransition = transition
    }

    private fun resetTransitions() {
        exitTransition = null
        reenterTransition = null
    }
}
