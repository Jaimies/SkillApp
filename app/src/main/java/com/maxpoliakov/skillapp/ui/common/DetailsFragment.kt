package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.CollapsingToolbarBinding
import com.maxpoliakov.skillapp.ui.MainActivity
import com.maxpoliakov.skillapp.util.transition.createMaterialContainerTransform
import com.maxpoliakov.skillapp.util.ui.setMarginTop

abstract class DetailsFragment(@MenuRes menuId: Int) : ActionBarFragment(menuId) {
    protected abstract val collapsingToolbarBinding: CollapsingToolbarBinding

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createMaterialContainerTransform(R.integer.animation_duration)
        sharedElementReturnTransition = createMaterialContainerTransform(R.integer.animation_duration_short)
    }

    @CallSuper
    override fun onStart() = collapsingToolbarBinding.run {
        super.onStart()
        appBar.post {
            appBar.setMarginTop(-collapsingToolbar.height)
        }

        Unit
    }

    @CallSuper
    override fun onResume() = collapsingToolbarBinding.run {
        super.onResume()
        (requireActivity() as MainActivity).setToolbar(collapsingToolbar)
        appBar.setMarginTop(0)

        appBar.post {
            appBar.setMarginTop(0)
        }

        Unit
    }


    @CallSuper
    override fun onPause() = collapsingToolbarBinding.run {
        super.onPause()
        appBar.setMarginTop(-collapsingToolbar.height)
        (requireActivity() as MainActivity).resetToolbar()
    }
}
