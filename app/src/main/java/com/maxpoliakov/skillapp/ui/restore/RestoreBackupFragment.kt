package com.maxpoliakov.skillapp.ui.restore

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RestoreBackupFragBinding
import com.maxpoliakov.skillapp.di.DaggerBackupComponent
import com.maxpoliakov.skillapp.di.FragmentBackupComponent
import com.maxpoliakov.skillapp.model.LoadingState
import com.maxpoliakov.skillapp.shared.DataBindingFragment
import com.maxpoliakov.skillapp.shared.fragment.observe
import com.maxpoliakov.skillapp.shared.recyclerview.addDividers
import com.maxpoliakov.skillapp.shared.recyclerview.setupAdapter
import javax.inject.Inject

class RestoreBackupFragment : DataBindingFragment<RestoreBackupFragBinding>() {
    override val layoutId get() = R.layout.restore_backup_frag

    private val viewModel : RestoreBackupViewModel by viewModels { RestoreBackupViewModel.Factory }

    @Inject
    lateinit var adapter: BackupListAdapter

    private lateinit var component: FragmentBackupComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val args = RestoreBackupFragmentArgs.fromBundle(requireArguments())

        component = DaggerBackupComponent
            .factory()
            .create(requireContext().applicationContext, args.backupBackend)
            .fragmentSubcomponentFactory()
            .create(this)

        component.inject(this)
    }

    override fun onBindingCreated(binding: RestoreBackupFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel

        binding.backupsList.setupAdapter(adapter)
        binding.backupsList.addDividers()

        observe(viewModel.backups) { state ->
            if (state is LoadingState.Success)
                adapter.submitList(state.value)
        }
    }

    override fun onPreDestroyBinding(binding: RestoreBackupFragBinding) {
        super.onPreDestroyBinding(binding)
        binding.backupsList.adapter = null
    }
}
