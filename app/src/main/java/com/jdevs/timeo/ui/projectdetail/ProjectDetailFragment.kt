package com.jdevs.timeo.ui.projectdetail


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.ProjectdetailFragBinding
import com.jdevs.timeo.ui.activities.RecordDialog
import com.jdevs.timeo.util.extensions.getAppComponent
import com.jdevs.timeo.util.extensions.observeEvent
import javax.inject.Inject

class ProjectDetailFragment : ActionBarFragment() {

    override val menuId = R.menu.activity_detail_fragment_menu
    private val args: ProjectDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: ProjectDetailViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.setProject(args.project)
        viewModel.setupProjectLiveData(args.project)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ProjectdetailFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        viewModel.apply {

            project.observe(viewLifecycleOwner) { project -> setProject(project) }

            observeEvent(showRecordDialog) {
                RecordDialog(context!!) { time -> addRecord(project.value!!, time) }.show()
            }
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.editActivity) {

            val directions = ProjectDetailFragmentDirections
                .actionProjectDetailFragmentToAddProjectFragment(project = viewModel.project.value)

            findNavController().navigate(directions)
            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }
}
