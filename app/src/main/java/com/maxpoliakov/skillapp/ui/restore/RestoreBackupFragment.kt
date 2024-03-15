package com.maxpoliakov.skillapp.ui.restore

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RestoreBackupFragBinding
import com.maxpoliakov.skillapp.model.LoadingState
import com.maxpoliakov.skillapp.shared.DataBindingFragment
import com.maxpoliakov.skillapp.shared.fragment.observe
import com.maxpoliakov.skillapp.shared.recyclerview.addDividers
import com.maxpoliakov.skillapp.shared.recyclerview.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestoreBackupFragment : DataBindingFragment<RestoreBackupFragBinding>() {
    override val layoutId get() = R.layout.restore_backup_frag

    private val viewModel: RestoreBackupViewModel by viewModels()

    @Inject
    lateinit var adapter: BackupListAdapter

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
