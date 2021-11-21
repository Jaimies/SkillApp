package com.maxpoliakov.skillapp.ui.skills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.get
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
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.SubscriptionState
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment
import com.maxpoliakov.skillapp.ui.common.CardViewDecoration
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupViewHolder
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.Change
import com.maxpoliakov.skillapp.util.ui.ItemTouchHelperCallback
import com.maxpoliakov.skillapp.util.ui.createReorderAndGroupItemTouchHelper
import com.maxpoliakov.skillapp.util.ui.createReorderItemTouchHelper
import com.maxpoliakov.skillapp.util.ui.findViewHolder
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import com.maxpoliakov.skillapp.util.ui.smoothScrollToTop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SkillsFragmentCallback {
    fun navigateToSkillDetail(view: View, skill: Skill)
    fun navigateToGroupDetail(view: View, group: SkillGroup)

    fun startDrag(viewHolder: RecyclerView.ViewHolder)
}

@AndroidEntryPoint
class SkillsFragment : ActionBarFragment(R.menu.skills_frag_menu), SkillsFragmentCallback {
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
                        name = getString(R.string.new_group),
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

        private fun findGroupViewHolderById(groupId: Int): SkillGroupViewHolder? {
            return binding.recyclerView.findViewHolder { viewHolder ->
                viewHolder.viewModel.skillGroup.value!!.id == groupId
            }
        }

        private fun findGroupFooterViewHolderById(groupId: Int): SkillGroupFooterViewHolder? {
            return binding.recyclerView.findViewHolder { viewHolder ->
                viewHolder.groupId == groupId
            }
        }

        private fun findSkillViewHolderById(skillId: Int): SkillViewHolder? {
            return binding.recyclerView.findViewHolder { viewHolder ->
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

    private var itemTouchHelper: ItemTouchHelper? = null

    private lateinit var binding: SkillsFragBinding

    @Inject
    lateinit var listAdapterFactory: SkillListAdapter.Factory

    private val listAdapter by lazy {
        listAdapterFactory.create(this)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        viewModel.subscriptionState.observe(viewLifecycleOwner) { state ->
            if (state == SubscriptionState.Loading) return@observe
            menu[0].isVisible = state == SubscriptionState.NotSubscribed
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.recyclerView.setupAdapter(listAdapter)
        binding.recyclerView.addItemDecoration(CardViewDecoration())

        lifecycleScope.launch {
            viewModel.isSubscribed.collect { isSubscribed ->
                itemTouchHelper?.attachToRecyclerView(null)
                itemTouchHelper = getItemTouchHelper(isSubscribed).apply {
                    attachToRecyclerView(binding.recyclerView)
                }
            }
        }

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
                            is SkillGroup -> listOf(item) + item.skills.sortedBy { it.order } + listOf(SkillGroupFooter(item.id))
                            else -> throw IllegalStateException("Orderables cannot be anything other than Skill or SkillGroup objects")
                        }
                    }

                if (listAdapter.currentList.run { isNotEmpty() && list.size > this.size }) {
                    whenResumed {
                        binding.recyclerView.smoothScrollToTop()
                    }
                }

                listAdapter.submitList(list)
            }
        }

        observe(viewModel.isActive, this::setStopwatchActive)

        observe(viewModel.navigateToAddEdit) {
            val transitionName = getString(R.string.add_skill_transition_name)
            val extras = FragmentNavigatorExtras(binding.addSkillFab to transitionName)
            val directions = MainDirections.actionToAddSkillFragment()
            navigateWithTransition(directions, extras)
        }
    }

    private fun getItemTouchHelper(isSubscribed: Boolean): ItemTouchHelper {
        return if (isSubscribed)
            createReorderAndGroupItemTouchHelper(itemTouchHelperCallback)
        else
            createReorderItemTouchHelper(itemTouchHelperCallback)
    }

    private fun setStopwatchActive(isActive: Boolean) {
        if (isActive) showStopwatch()
        else hideStopwatch()
    }

    private fun showStopwatch() {
        listAdapter.showStopwatch()
        binding.recyclerView.smoothScrollToTop()
    }

    private fun hideStopwatch() = listAdapter.hideStopwatch()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.go_to_premium) {
            findNavController().navigateAnimated(R.id.premium_fragment_dest)
            return true
        }

        return false
    }

    override fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper?.startDrag(viewHolder)
    }

    override fun navigateToSkillDetail(view: View, skill: Skill) {
        val directions = MainDirections.actionToSkillDetailFragment(skill.id)
        val transitionName = getString(R.string.skill_transition_name)
        val extras = FragmentNavigatorExtras(view to transitionName)
        navigateWithTransition(directions, extras)
    }

    override fun navigateToGroupDetail(view: View, group: SkillGroup) {
        val directions = MainDirections.actionToSkillGroupFragment(group.id)
        val transitionName = getString(R.string.group_transition_name)
        val extras = FragmentNavigatorExtras(view to transitionName)
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
