package com.jdevs.timeo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.FragmentActivityDetailBinding
import com.jdevs.timeo.ui.activities.viewmodel.ActivityDetailViewModel

class ActivityDetailFragment : ActionBarFragment() {

    override val menuId = R.menu.action_bar_activity_details
    private val args: ActivityDetailFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ActivityDetailViewModel::class.java).apply {
            setActivity(args.timeoActivity)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentActivityDetailBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = this
            it.viewmodel = viewModel
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.editActivity) {
            val directions =
                ActivityDetailFragmentDirections.actionEditActivity(
                    true,
                    args.activityId,
                    args.timeoActivity
                )

            findNavController().navigate(directions)

            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }
}
