package com.theskillapp.skillapp.ui.restore

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.data.di.BackupBackend
import com.theskillapp.skillapp.data.di.BackupComponent
import com.theskillapp.skillapp.databinding.RestoreBackupFragBinding
import com.theskillapp.skillapp.model.LoadingState
import com.theskillapp.skillapp.shared.DataBindingFragment
import com.theskillapp.skillapp.shared.fragment.observe
import com.theskillapp.skillapp.shared.recyclerview.addDividers
import com.theskillapp.skillapp.shared.recyclerview.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestoreBackupFragment : DataBindingFragment<RestoreBackupFragBinding>() {
    override val layoutId get() = R.layout.restore_backup_frag

    private val viewModel: RestoreBackupViewModel by viewModels()

    lateinit var adapter: BackupListAdapter

    @Inject
    lateinit var backupComponentsByBackend: Map<BackupBackend, @JvmSuppressWildcards BackupComponent>

    @Inject
    lateinit var adapterFactory: BackupListAdapter.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val args = RestoreBackupFragmentArgs.fromBundle(requireArguments())
        val useCase = backupComponentsByBackend[args.backupBackend]!!.restoreBackupUseCase()
        adapter = adapterFactory.create(useCase)
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
