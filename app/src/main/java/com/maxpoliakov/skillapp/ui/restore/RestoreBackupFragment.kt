package com.maxpoliakov.skillapp.ui.restore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RestoreBackupFragBinding
import com.maxpoliakov.skillapp.ui.common.BaseFragment
import com.maxpoliakov.skillapp.util.dialog.showSnackbar
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.addDividers
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestoreBackupFragment : BaseFragment() {
    private lateinit var binding: RestoreBackupFragBinding

    private val viewModel: RestoreBackupViewModel by viewModels()

    @Inject
    lateinit var adapter: BackupListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = RestoreBackupFragBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backupsList.setupAdapter(adapter)
        binding.backupsList.addDividers()
        observe(viewModel.backups, adapter::submitList)
        observe(viewModel.restorationSucceeded) { findNavController().navigateUp() }
        observe(viewModel.showError) {
            showSnackbar(R.string.something_went_wrong)
        }
    }
}
