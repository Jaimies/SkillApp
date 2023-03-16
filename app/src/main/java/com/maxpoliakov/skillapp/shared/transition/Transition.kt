package com.maxpoliakov.skillapp.shared.transition

import android.content.Context
import android.graphics.Color
import androidx.annotation.IntegerRes
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.extensions.getColorAttributeValue

fun createMaterialContainerTransform(
    context: Context,
    @IntegerRes durationResId: Int
): MaterialContainerTransform {
    return MaterialContainerTransform().apply {
        drawingViewId = R.id.nav_host_container
        setPathMotion(MaterialArcMotion())
        scrimColor = Color.TRANSPARENT
        duration = context.resources.getInteger(durationResId).toLong()
        setAllContainerColors(context.getColorAttributeValue(R.attr.colorSurface))
    }
}

fun Fragment.createMaterialContainerTransform(@IntegerRes durationResId: Int): MaterialContainerTransform {
    return createMaterialContainerTransform(requireContext(), durationResId)
}
