package com.jdevs.timeo.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.jdevs.timeo.databinding.ProfileFragBinding
import com.jdevs.timeo.util.charts.setup
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.ui.setState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.profile_frag.chart

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ProfileFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val chart = chart as LineChart
        chart.setup()
        observe(viewModel.stats, chart::setState)
    }
}
