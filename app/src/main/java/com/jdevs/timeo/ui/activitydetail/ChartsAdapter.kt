package com.jdevs.timeo.ui.activitydetail

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.util.charts.ChartData
import com.jdevs.timeo.util.charts.setup
import com.jdevs.timeo.util.view.inflate
import com.jdevs.timeo.util.view.setData

class ChartsAdapter(private vararg val chartLiveData: LiveData<ChartData>) :
    RecyclerView.Adapter<ChartsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.activitydetail_chart))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.setLiveData(chartLiveData[position])
    }

    override fun getItemCount() = chartLiveData.size

    class ViewHolder(view: View) : BaseViewHolder(view) {

        private val chart = (view as LineChart).apply { setup() }

        fun setLiveData(liveData: LiveData<ChartData>) {

            liveData.observe(lifecycleOwner) { chart.setData(it) }
        }
    }
}
