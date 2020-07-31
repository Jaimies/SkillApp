package com.jdevs.timeo.ui.common.adapter

import com.jdevs.timeo.model.ViewItem

interface BaseAdapter {
    fun getItem(position: Int): ViewItem
}