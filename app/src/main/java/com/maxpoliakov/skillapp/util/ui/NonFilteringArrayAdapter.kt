package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class NonFilteringArrayAdapter<T>(
    context: Context, textViewResourceId: Int,
    private val items: List<T>
) : ArrayAdapter<T>(context, textViewResourceId, items) {

    private val filter = NonFilteringFilter()

    override fun getFilter(): Filter {
        return filter
    }

    private inner class NonFilteringFilter : Filter() {
        override fun performFiltering(arg0: CharSequence?): FilterResults {
            return FilterResults().apply {
                values = items
                count = items.size
            }
        }

        override fun publishResults(arg0: CharSequence?, arg1: FilterResults?) {
            notifyDataSetChanged()
        }
    }
}