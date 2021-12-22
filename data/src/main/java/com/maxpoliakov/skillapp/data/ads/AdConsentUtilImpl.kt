package com.maxpoliakov.skillapp.data.ads

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation.ConsentStatus
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdConsentUtilImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    var consentState = ConsentState.Loading
        private set

    private val consentInformation = UserMessagingPlatform.getConsentInformation(context)
    private var consentForm: ConsentForm? = null
    private var consentUpdateRequested = false

    fun updateConsentInformation(activity: Activity) {
        if (consentUpdateRequested) return

        consentUpdateRequested = true

        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(true)
            .build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                val canShowAds = consentInformation.consentStatus != ConsentStatus.REQUIRED

                consentState =
                    if (canShowAds) ConsentState.CanShowAds
                    else ConsentState.MustRequestConsent

                if (consentInformation.isConsentFormAvailable) loadForm();
            }, {})
    }

    fun showConsentFormIfNecessary(activity: Activity, onDismissed: (FormError?) -> Unit) {
        consentForm?.let { form ->
            if (consentState == ConsentState.MustRequestConsent)
                form.show(activity) { formError ->
                    if (formError == null)
                        consentState = ConsentState.CanShowAds

                    onDismissed(formError)
                }
        }
    }

    private fun loadForm() {
        UserMessagingPlatform.loadConsentForm(
            context,
            { consentForm ->
                this@AdConsentUtilImpl.consentForm = consentForm
            }, {}
        )
    }

    enum class ConsentState {
        Loading, CanShowAds, MustRequestConsent,
    }
}
