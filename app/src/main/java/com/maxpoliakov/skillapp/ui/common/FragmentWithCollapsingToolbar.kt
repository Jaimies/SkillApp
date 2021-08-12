package com.maxpoliakov.skillapp.ui.common

import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.lifecycle.lifecycleScope
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.CollapsingToolbarBinding
import com.maxpoliakov.skillapp.ui.MainActivity
import com.maxpoliakov.skillapp.util.ui.setMarginTop
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class FragmentWithCollapsingToolbar(@MenuRes menuId: Int) : ActionBarFragment(menuId) {
    protected abstract val collapsingToolbarBinding: CollapsingToolbarBinding

    @CallSuper
    override fun onStart() = collapsingToolbarBinding.run {
        super.onStart()
        appBar.post {
            appBar.setMarginTop(-collapsingToolbar.height)
        }

        lifecycleScope.launch {
            awaitUntilAnimationFinished()
            (requireActivity() as MainActivity).setToolbar(collapsingToolbar)
            appBar.setMarginTop(0)
        }

        Unit
    }

    @CallSuper
    override fun onStop() = collapsingToolbarBinding.run {
        super.onStop()
        (requireActivity() as MainActivity).resetToolbar()
    }

    private suspend fun awaitUntilAnimationFinished() {
        val animationDuration = requireContext().resources.getInteger(R.integer.animation_duration)
        delay(animationDuration.toLong())
    }
}
