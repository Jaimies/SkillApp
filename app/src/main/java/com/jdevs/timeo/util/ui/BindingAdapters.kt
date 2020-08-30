package com.jdevs.timeo.util.ui

import android.R.layout.simple_list_item_1
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.util.Consumer
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@BindingAdapter("visible")
fun View.isVisible(value: Boolean) {
    isVisible = value
}

@BindingAdapter("viewpager", "entries")
fun TabLayout.setupWithViewPager(viewPager: ViewPager2, tabsResId: Int) {
    val tabs = context.resources.getStringArray(tabsResId)

    TabLayoutMediator(this, viewPager) { tab, position ->
        tab.text = tabs[position]
    }.attach()
}

@BindingAdapter("entries")
fun AutoCompleteTextView.setEntries(entries: List<*>?) {
    if (entries != null) {
        setAdapter(ArrayAdapter(context, simple_list_item_1, entries))
    }
}

@BindingAdapter("onSelectedItemChanged")
fun AutoCompleteTextView.setOnSelectedItemPositionChanged(onChanged: Consumer<Int>) {
    setOnItemClickListener { _, _, position, _ -> onChanged.accept(position) }
    doAfterTextChanged { onChanged.accept(-1) }
}
