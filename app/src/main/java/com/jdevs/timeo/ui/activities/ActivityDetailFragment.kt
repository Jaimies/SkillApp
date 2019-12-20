package com.jdevs.timeo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.ActivitydetailFragBinding
import com.jdevs.timeo.ui.activities.viewmodel.ActivityDetailViewModel

class ActivityDetailFragment : ActionBarFragment() {

    override val menuId = R.menu.activity_detail_fragment_menu
    private val args: ActivityDetailFragmentArgs by navArgs()

    private val viewModel: ActivityDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ActivitydetailFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = this
            it.viewmodel = viewModel
        }

        viewModel.setActivity(args.activity)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.editActivity) {

            val directions =
                ActivityDetailFragmentDirections.actionActivityDetailFragmentToAddEditActivityFragment(
                    activity = args.activity,
                    id = args.id,
                    isEdited = true
                )

            findNavController().navigate(directions)

            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }
}
