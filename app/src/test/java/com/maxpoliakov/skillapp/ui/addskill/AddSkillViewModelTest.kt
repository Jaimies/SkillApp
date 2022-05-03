package com.maxpoliakov.skillapp.ui.addskill

import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import com.maxpoliakov.skillapp.setupThreads
import com.maxpoliakov.skillapp.test.any
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.Duration
import java.time.Duration.ZERO
import java.time.LocalDateTime

class StubBillingRepository: ExtendedBillingRepository {
    override fun addOneTimePurchaseUpdateListener(listener: PurchasesUpdatedListener) {}
    override suspend fun getSubscriptionSkuDetails(): SkuDetails? = null

    override val subscriptionState = MutableStateFlow(BillingRepository.SubscriptionState.Subscribed)
    override val connectionState = BillingRepository.ConnectionState.Connected

    override suspend fun connect() {}
    override suspend fun getSubscriptionExpirationTime(): LocalDateTime? = null
}

class AddSkillViewModelTest : StringSpec({
    lateinit var viewModel: AddSkillViewModel
    lateinit var useCase: AddSkillUseCase
    lateinit var billingRepository: ExtendedBillingRepository

    beforeSpec { setupThreads() }

    beforeEach {
        useCase = mock(AddSkillUseCase::class.java)
        `when`(useCase.run(any())).thenReturn(1)
        billingRepository = StubBillingRepository()
        viewModel = AddSkillViewModel(useCase, billingRepository)
    }

    "addSkill()" {
        viewModel.totalTime.value = "100"
        viewModel.name.value = "Skill name "
        viewModel.update()
        verify(useCase).run(Skill("Skill name", MeasurementUnit.Millis, Duration.ofHours(100).toMillis(), Duration.ofHours(100).toMillis()))
    }

    "addSkill() uses ZERO as the time if the totalTime field is null" {
        viewModel.totalTime.value = null
        viewModel.name.value = "Name"
        viewModel.update()
        verify(useCase).run(Skill("Name", MeasurementUnit.Millis, 0, 0))
    }

    "addSkill() uses ZERO as the time if the totalTime field is empty" {
        viewModel.totalTime.value = ""
        viewModel.name.value = "Name"
        viewModel.update()
        verify(useCase).run(Skill("Name", MeasurementUnit.Millis, 0, 0))
    }
})
