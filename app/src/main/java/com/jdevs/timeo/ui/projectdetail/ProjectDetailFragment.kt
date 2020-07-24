package com.jdevs.timeo.ui.projectdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProjectdetailFragBinding
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.util.lifecycle.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProjectDetailFragment : ActionBarFragment(R.menu.activitydetail_frag_menu) {

    private val args: ProjectDetailFragmentArgs by navArgs()
    private val viewModel by viewModels { viewModelFactory.create(args.project.id) }

    @Inject
    lateinit var viewModelFactory: ProjectDetailViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ProjectdetailFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.editActivity) {
            val directions = ProjectDetailFragmentDirections
                .actionProjectDetailFragmentToAddProjectFragment(viewModel.project.value!!)

            findNavController().navigate(directions)
            return true
        }

        return false
    }
}
