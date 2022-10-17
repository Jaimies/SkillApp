package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.common.history.FragmentWithHistory
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.hardware.showKeyboard
import com.maxpoliakov.skillapp.util.transition.createMaterialContainerTransform
import com.maxpoliakov.skillapp.util.ui.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class DetailsFragment(@MenuRes menuId: Int) : FragmentWithHistory(menuId) {
    protected abstract val saveBtn: Button
    protected abstract val input: EditText
    protected abstract val content: ViewGroup
    protected abstract val goalInput: View
    protected abstract val history: View

    abstract override val viewModel: DetailsViewModel

    protected open fun onDeleteSelected() {}

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createMaterialContainerTransform(R.integer.animation_duration)
        sharedElementReturnTransition = createMaterialContainerTransform(R.integer.animation_duration_short)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.getBoolean(IS_IN_EDITING_MODE, false))
            startEditing()

        observe(viewModel.onSave) { stopEditing() }

        observe(viewModel.chooseGoal) {
            viewModel.unit.value!!.showGoalPicker(
                requireContext(),
                viewModel.goal.value?.mapToDomain(),
                onGoalSet = viewModel::setGoal
            )
        }

        saveBtn.isGone = true
        input.isFocusable = false
        input.isFocusableInTouchMode = false

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = !onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_IN_EDITING_MODE, viewModel.isEditing.value ?: false)
    }

    private fun onBackPressed(): Boolean {
        if (viewModel.isEditing.value!!) {
            viewModel.exitEditingMode()
            stopEditing()
            return false
        } else {
            findNavController().navigateUp()
            return true
        }
    }

    override fun onHistoryEmpty() {
        history.isVisible = false
    }

    override fun onHistoryNotEmpty() {
        history.isVisible = true
    }

    @CallSuper
    override fun onMenuItemSelected(id: Int): Boolean {
        return when(id) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.edit -> {
                onEditClicked()
                true
            }
            R.id.delete -> {
                onDeleteSelected()
                true
            }
            else -> false
        }
    }

    private fun onEditClicked() {
        if (viewModel.isEditing.value!!)
            viewModel.save()
        else
            startEditing()
    }

    open fun onStartEditing() {}

    protected open fun startEditing() = input.run {
        onStartEditing()

        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        setSelection(text.length)

        viewModel.enterEditingMode()

        lifecycleScope.launchWhenResumed {
            delay(100)
            runCatching { menu?.getItem(0)?.setTitle(R.string.save) }
        }

        val duration = transitionDuration
        content.animate()
            .alpha(0f)
            .translationY(30.dp.toPx(requireContext()).toFloat())
            .setDuration(duration)
            .start()

        lifecycleScope.launch {
            delay(duration)
            content.isGone = true
            showKeyboard()
            goalInput.isVisible = true
            goalInput.alpha = 0f
            goalInput.translationY = 0f
            goalInput.animate()
                .setDuration(transitionDuration)
                .translationY(-30.dp.toPx(requireContext()).toFloat())
                .alpha(1f)
                .start()
        }

        saveBtn.isGone = false
        saveBtn.alpha = 0f

        saveBtn.animate()
            .alpha(1f)
            .setDuration(duration)
            .start()
    }

    protected open fun stopEditing() = input.run {
        isFocusable = false
        isFocusableInTouchMode = false
        clearFocus()

        menu?.getItem(0)?.setTitle(R.string.edit)
        val duration = shortTransitionDuration

        content.isGone = false

        content.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(duration)
            .start()

        saveBtn.animate()
            .alpha(0f)
            .setDuration(duration)
            .start()

        lifecycleScope.launch {
            delay(duration)
            saveBtn.isGone = true
            hideKeyboard()
        }

        goalInput.isVisible = false
    }

    protected val transitionDuration
        get() = resources.getInteger(R.integer.animation_duration).toLong()

    protected val shortTransitionDuration
        get() = resources.getInteger(R.integer.animation_duration_short).toLong()

    companion object {
        private const val IS_IN_EDITING_MODE = "IS_IN_EDITING_MODE"
    }
}
