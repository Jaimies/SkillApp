package com.maxpoliakov.skillapp.ui.premium

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.billing.BillingRepository
import com.maxpoliakov.skillapp.data.billing.BillingRepository.Companion.SUBSCRIPTION_SKU_NAME
import com.maxpoliakov.skillapp.databinding.PremiumFragBinding
import com.maxpoliakov.skillapp.util.dialog.showSnackbar
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.subscriptions.showSubscriptionPrompt
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PremiumFragment : Fragment() {
    private val viewModel: PremiumViewModel by viewModels()

    @Inject
    lateinit var billingRepository: BillingRepository

    @Inject
    lateinit var billingClient: BillingClient

    private lateinit var binding: PremiumFragBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = PremiumFragBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(viewModel.showSubscriptionPrompt) {
            lifecycleScope.launch {
                billingClient.showSubscriptionPrompt(billingRepository, requireActivity(), binding.snackbarRoot)
            }
        }

        observe(viewModel.goToManageSubscriptions) {
            val uri = getString(R.string.subscriptions_uri, SUBSCRIPTION_SKU_NAME, requireContext().packageName)
                .let { uri -> Uri.parse(uri) }

            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        observe(viewModel.showError) {
            binding.snackbarRoot.showSnackbar(R.string.something_went_wrong)
        }
    }
}
