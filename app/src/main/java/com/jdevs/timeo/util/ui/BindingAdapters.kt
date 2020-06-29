@file:Suppress("MatchingDeclarationName")

package com.jdevs.timeo.util.ui

import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.annotation.ArrayRes
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.common.SignInButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@BindingAdapter("visible")
fun View.visible(value: Boolean) {
    isVisible = value
}

@BindingAdapter("android:onClick")
fun SignInButton.setOnClickListener(onClick: Runnable) = setOnClickListener { onClick.run() }

@BindingAdapter("onEnterPressed")
fun EditText.setOnEnterPressedListener(block: Runnable) {

    setOnEditorActionListener { _, _, _ ->
        block.run()
        true
    }
}

@BindingAdapter("viewpager", "entries")
fun TabLayout.setupWithViewPager(viewPager: ViewPager2, @ArrayRes itemsResId: Int) {
    val items = context.resources.getStringArray(itemsResId)
    TabLayoutMediator(this, viewPager) { tab, position -> tab.text = items[position] }.attach()
}

@BindingAdapter("entries")
fun AutoCompleteTextView.setEntries(entries: List<Any>?) {
    if (entries == null) return
    setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, entries))
}

interface OnSelectedItemChangedListener {
    fun onChanged(newPosition: Int?)
}

@BindingAdapter("onSelectedItemChanged")
fun AutoCompleteTextView.setOnSelectedItemPositionChanged(onChanged: OnSelectedItemChangedListener) {
    setOnItemClickListener { _, _, position, _ -> onChanged.onChanged(position) }
    doAfterTextChanged { onChanged.onChanged(null) }
}
