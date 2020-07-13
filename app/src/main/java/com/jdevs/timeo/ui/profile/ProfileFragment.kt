package com.jdevs.timeo.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProfileFragBinding
import com.jdevs.timeo.ui.activitydetail.ChartsAdapter
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.util.fragment.navigateToGraph
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activitydetail_frag.stats_viewpager

@AndroidEntryPoint
class ProfileFragment : ActionBarFragment(R.menu.profile_frag_menu) {
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
        stats_viewpager.adapter =
            ChartsAdapter(viewModel.dayStats, viewModel.weekStats, viewModel.monthStats)

        observe(viewModel.navigateToSignIn) { findNavController().navigateAnimated(R.id.signin_fragment_dest) }
        observe(viewModel.navigateToOverview) { navigateToGraph(R.id.overview) }
    }
}
