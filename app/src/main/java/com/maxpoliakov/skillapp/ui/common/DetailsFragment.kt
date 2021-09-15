package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.util.transition.createMaterialContainerTransform

abstract class DetailsFragment(@MenuRes menuId: Int) : ActionBarFragment(menuId) {
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createMaterialContainerTransform(R.integer.animation_duration)
        sharedElementReturnTransition = createMaterialContainerTransform(R.integer.animation_duration_short)
    }
}
