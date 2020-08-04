package com.jdevs.timeo.ui.activitydetail

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.util.charts.ChartState
import com.jdevs.timeo.util.charts.setup
import com.jdevs.timeo.util.ui.inflate
import com.jdevs.timeo.util.ui.setState

class ChartsAdapter(private vararg val chartLiveDatas: LiveData<ChartState?>) :
    RecyclerView.Adapter<ChartsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.activitydetail_chart) as LineChart)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setLiveData(chartLiveDatas[position])
    }

    override fun getItemCount() = chartLiveDatas.size

    class ViewHolder(private val chart: LineChart) : BaseViewHolder(chart) {

        init {
            chart.setup()
        }

        fun setLiveData(liveData: LiveData<ChartState?>) {
            liveData.observe(chart::setState)
        }
    }
}
