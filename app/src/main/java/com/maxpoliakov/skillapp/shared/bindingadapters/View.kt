package com.maxpoliakov.skillapp.shared.bindingadapters

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.maxpoliakov.skillapp.shared.extensions.getColorAttributeValue

@BindingAdapter("visible")
fun View.isVisible(value: Boolean) {
    isVisible = value
}

@BindingAdapter("layout_sideMargin")
fun setSideMargins(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = dimen.toInt()
    layoutParams.marginEnd = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("layout_marginBottom")
fun View.setBottomMargin(dimen: Float) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.bottomMargin = dimen.toInt()
    this.layoutParams = layoutParams
}

@BindingAdapter("backgroundTintAttr")
fun View.setBackgroundTintAttr(attrValue: Int) {
    setBackgroundColor(context.getColorAttributeValue(attrValue))
}

@BindingAdapter(
    "layout_constraint_startSide",
    "layout_constraint_toEndId",
    "layout_constraint_endSide",
)
fun View.setConditionalConstraint(
    startSide: Int, endId: Int, endSide: Int,
) {
    val constraintLayout = (parent as? ConstraintLayout) ?: return
    with(ConstraintSet()) {
        clone(constraintLayout)
        connect(id, startSide, endId, endSide)
        applyTo(constraintLayout)
    }
}
