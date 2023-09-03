package com.maxpoliakov.skillapp.ui.skills

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialElevationScale
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillsFragBinding
import com.maxpoliakov.skillapp.domain.model.Orderable
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.model.Intro
import com.maxpoliakov.skillapp.shared.ActionBarFragment
import com.maxpoliakov.skillapp.shared.fragment.observe
import com.maxpoliakov.skillapp.shared.recyclerview.Change
import com.maxpoliakov.skillapp.shared.recyclerview.ItemTouchHelperCallback
import com.maxpoliakov.skillapp.shared.recyclerview.SimpleCallbackImpl
import com.maxpoliakov.skillapp.shared.recyclerview.itemdecoration.fakecardview.FakeCardViewDecoration
import com.maxpoliakov.skillapp.shared.recyclerview.scrollToTop
import com.maxpoliakov.skillapp.shared.recyclerview.setupAdapter
import com.maxpoliakov.skillapp.shared.tracking.RecordUtil
import com.maxpoliakov.skillapp.ui.MainActivity
import com.maxpoliakov.skillapp.ui.intro.IntroUtil
import com.maxpoliakov.skillapp.ui.intro.Intro_3_1_0
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListAdapter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListAdapter.Companion.getGroupFooterItemId
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListAdapter.Companion.getGroupItemId
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListAdapter.Companion.getSkillItemId
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListMarginDecoration
import com.maxpoliakov.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooter
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
class SkillsFragment : ActionBarFragment<SkillsFragBinding>(R.menu.skills_frag_menu), SkillsFragmentCallback, ListListener<Any> {
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
            lastItemDropTime = System.nanoTime()

            when (change) {
                is Change.CreateGroup -> createGroup(change)
                is Change.AddToGroup -> addToGroup(change)
                null -> {}
            }

            val list = listAdapter.currentList.filterIsInstance<Orderable>()
            viewModel.updateOrder(list)
        }

        private fun addToGroup(change: Change.AddToGroup) {
            viewModel.addToGroup(change.skill, change.groupId)

            updateOldGroupIfNeeded(change.skill)
            addSkillToGroup(change)
            updateGroupIdOfSkill(change.skill, change.groupId)
        }

        private fun createGroup(change: Change.CreateGroup) {
            val groupHeaderPosition = listAdapter.getPositionOfFirst(change.skill, change.otherSkill)

            val group = SkillGroup(
                id = 0,
                name = getString(R.string.new_group),
                skills = listOf(change.skill, change.otherSkill),
                goal = null,
                order = -1,
                unit = change.skill.unit,
            )

            val groupFooterPosition = groupHeaderPosition + group.skills.size + 1

            lifecycleScope.launch {
                val groupId = viewModel.createGroupAsync(change.skill, group).await().toInt()
                val newGroup = group.copy(id = groupId)

                updateGroupIdOfSkill(change.skill, groupId)
                updateGroupIdOfSkill(change.otherSkill, groupId)

                val updatedList = listAdapter.currentList.toMutableList().apply {
                    add(groupHeaderPosition, newGroup)
                    add(groupFooterPosition, SkillGroupFooter(newGroup))
                }

                listAdapter.setListWithoutDiffing(updatedList)
                listAdapter.notifyItemInserted(groupHeaderPosition)
                listAdapter.notifyItemInserted(groupFooterPosition)
            }
        }

        private fun addSkillToGroup(change: Change.AddToGroup) {
            listAdapter.bindItemWithItemIdToViewHolder<SkillGroup>(getGroupItemId(change.groupId), binding?.recyclerView) { group ->
                group.copy(skills = group.skills + change.skill)
            }
        }

        private fun updateGroupIdOfSkill(skill: Skill, newGroupId: Int) {
            listAdapter.bindItemWithItemIdToViewHolder<Skill>(getSkillItemId(skill.id), binding?.recyclerView) { skill ->
                skill.copy(groupId = newGroupId)
            }
        }

        private fun updateOldGroupIfNeeded(skill: Skill) {
            if (skill.isNotInAGroup) return

            val group = listAdapter.findItemByItemId(getGroupItemId(skill.groupId)) as? SkillGroup ?: return

            if (group.skills.size <= 1)
                deleteGroup(group)
            else
                removeSkillFromGroup(group, skill)
        }

        private fun deleteGroup(group: SkillGroup) {
            val position = listAdapter.getPositionOf(group)
            val footerPosition = listAdapter.findItemPositionByItemId(getGroupFooterItemId(group.id))

            val updatedList = listAdapter.currentList.toMutableList().apply {
                if (footerPosition != -1) removeAt(footerPosition)
                removeAt(position)
            }

            listAdapter.setListWithoutDiffing(updatedList)

            if (footerPosition != -1) listAdapter.notifyItemRemoved(footerPosition)
            listAdapter.notifyItemRemoved(position)
        }

        private fun removeSkillFromGroup(group: SkillGroup, skill: Skill) {
            listAdapter.bindItemWithItemIdToViewHolder<SkillGroup>(getGroupItemId(group.id), binding?.recyclerView) { item ->
                item.copy(skills = item.skills.filter { it.id != skill.id })
            }
        }
    }

    @Inject
    lateinit var listAdapterFactory: SkillListAdapter.Factory

    @Inject
    lateinit var introUtil: IntroUtil

    @Inject
    lateinit var intro: Intro_3_1_0

    @Inject
    lateinit var recordUtil: RecordUtil

    private val viewModel: SkillsViewModel by viewModels()

    private lateinit var listAdapter: SkillListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val toolbar get() = (requireActivity() as MainActivity).toolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listAdapter = listAdapterFactory.create(this).also { adapter ->
            adapter.addListListener(this)
        }

        itemTouchHelper = ItemTouchHelper(SimpleCallbackImpl(itemTouchHelperCallback, listAdapter))
    }

    override fun onBindingCreated(binding: SkillsFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        binding.viewModel = viewModel

        disableReenterTransitionIfSwitchedBottomNavigationViewTabs()
        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }

        binding.recyclerView.setupAdapter(listAdapter)
        binding.recyclerView.addItemDecoration(FakeCardViewDecoration())
        binding.recyclerView.addItemDecoration(SkillListMarginDecoration())

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        introUtil.showIfNecessary(Intro.Intro_3_1_0) { showIntro() }

        lifecycleScope.launch {
            viewModel.list.collect { list ->
                // Don't update within 0.5sec after the drag and drop has finished, as those updates cause awful transitions
                if (System.nanoTime() - lastItemDropTime < 500_000_000)
                    return@collect

                listAdapter.submitList(list)
            }
        }

        observe(viewModel.navigateToAddSkill) { navigateToAddSkill() }

        observe(viewModel.isEmpty) { isEmpty ->
            editMenuItem?.let { item ->
                if (item.isVisible != !isEmpty) requireActivity().invalidateOptionsMenu()
            }
        }

        observe(viewModel.navigateToSkillDetail) { skillAndItemId ->
            navigateToSkillDetail(skillAndItemId.first, skillAndItemId.second)
        }

        observe(viewModel.showRecordsAdded, recordUtil::notifyRecordsAdded)
    }

    override fun onPreDestroyBinding(binding: SkillsFragBinding) {
        super.onPreDestroyBinding(binding)
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

    override fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        if (!viewModel.isInEditingMode) return

        viewHolder.itemView.translationZ = 10f
        itemTouchHelper.startDrag(viewHolder)
    }

    private fun navigateToSkillDetail(skill: Skill, itemId: Long) {
        val view = binding?.recyclerView?.findViewHolderForItemId(itemId)?.itemView ?: return
        navigateToSkillDetail(view, skill)
    }

    override fun navigateToSkillDetail(view: View, skill: Skill) {
        val directions = MainDirections.actionToSkillDetailFragment(skill.id)
        navigate(view, directions, R.string.skill_detail_root_transition_name)
    }

    override fun navigateToGroupDetail(view: View, group: SkillGroup) {
        val directions = MainDirections.actionToSkillGroupFragment(group.id)
        navigate(view, directions, R.string.group_detail_root_transition_name)
    }

    override fun onCurrentListChanged(previousList: MutableList<Any>, currentList: MutableList<Any>) {
        if (skillAdded(previousList, currentList)) {
            binding?.recyclerView?.scrollToTop()
        }
    }

    private fun skillAdded(previousList: List<Any>, currentList: List<Any>): Boolean {
        return previousList.isNotEmpty()
                && currentList.count { it is Skill } > previousList.count { it is Skill }
    }

    // Navigation component defines its own transitions for BottomNavigationView.
    // Our reenterTransition would interfere with their transitions, so we need to disable it.
    private fun disableReenterTransitionIfSwitchedBottomNavigationViewTabs() {
        if (switchedBottomNavigationViewTabs()) {
            reenterTransition = null
        }
    }

    private fun switchedBottomNavigationViewTabs(): Boolean {
        return findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>(MainActivity.SWITCHED_BOTTOM_NAV_VIEW_TABS) ?: false
    }

    private fun navigateToAddSkill() {
        val directions = MainDirections.actionToAddSkillFragment()
        val view = binding?.addSkillFab ?: return
        navigate(view, directions, R.string.add_skill_transition_name)
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
            exitTransition = null
        }
    }

    private fun setupTransitions() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.animation_duration).toLong()
        }

        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.animation_duration_short).toLong()
        }
    }
}
