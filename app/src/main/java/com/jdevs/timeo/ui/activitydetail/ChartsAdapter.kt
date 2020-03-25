package com.jdevs.timeo.ui.activitydetail

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.util.charts.ChartData
import com.jdevs.timeo.util.charts.setup
import com.jdevs.timeo.util.view.inflate
import com.jdevs.timeo.util.view.setData
import kotlinx.android.synthetic.main.activitydetail_chart.view.line_chart

class ChartsAdapter(private vararg val chartLiveData: LiveData<ChartData>) :
    RecyclerView.Adapter<ChartsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.activitydetail_chart))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.setLiveData(chartLiveData[position])
    }

    override fun getItemCount() = CHART_TYPES_COUNT

    class ViewHolder(private val view: View) : BaseViewHolder(view) {

        init {
            view.line_chart.setup()
        }

        fun setLiveData(liveData: LiveData<ChartData>) {

            liveData.observe(lifecycleOwner) { view.line_chart.setData(it) }
        }
    }

    companion object {
        private const val CHART_TYPES_COUNT = 3
    }
}
