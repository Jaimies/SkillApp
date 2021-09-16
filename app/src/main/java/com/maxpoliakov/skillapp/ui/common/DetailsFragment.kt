package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.hardware.showKeyboard
import com.maxpoliakov.skillapp.util.transition.createMaterialContainerTransform
import com.maxpoliakov.skillapp.util.ui.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class DetailsFragment(@MenuRes menuId: Int) : ActionBarFragment(menuId) {
    protected abstract val saveBtn: Button
    protected abstract val input: EditText
    protected abstract val content: ViewGroup

    protected abstract val viewModel: DetailsViewModel

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createMaterialContainerTransform(R.integer.animation_duration)
        sharedElementReturnTransition = createMaterialContainerTransform(R.integer.animation_duration_short)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(viewModel.onSave) { stopEditing() }

        saveBtn.isGone = true
        input.isFocusable = false
        input.isFocusableInTouchMode = false

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = !onBackPressed()
        }
    }

    private fun onBackPressed(): Boolean {
        if (viewModel.isEditing.value!!) {
            stopEditing()
            return false
        } else {
            findNavController().navigateUp()
            return true
        }
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        if (item.itemId == R.id.edit) {
            if (viewModel.isEditing.value!!)
                stopEditing()
            else
                startEditing()

            return true
        }

        return false
    }

    protected fun startEditing() = input.run {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        setSelection(text.length)

        viewModel.enterEditingMode()

        menu.getItem(0).setTitle(R.string.save)

        val duration = resources.getInteger(R.integer.animation_duration).toLong()
        content.animate()
            .alpha(0f)
            .translationYBy(30.dp.toPx(requireContext()).toFloat())
            .setDuration(duration)
            .start()

        lifecycleScope.launch {
            delay(duration)
            content.isGone = true
            showKeyboard()
        }

        saveBtn.isGone = false
        saveBtn.alpha = 0f

        saveBtn.animate()
            .alpha(1f)
            .setDuration(duration)
            .start()
    }

    protected fun stopEditing() = input.run {
        isFocusable = false
        isFocusableInTouchMode = false
        clearFocus()

        viewModel.exitEditingMode()
        menu.getItem(0).setTitle(R.string.edit)
        val duration = resources.getInteger(R.integer.animation_duration).toLong()

        content.isGone = false

        content.animate()
            .alpha(1f)
            .translationYBy(-30.dp.toPx(context).toFloat())
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
    }
}
