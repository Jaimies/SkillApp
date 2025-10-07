package com.theskillapp.skillapp.shared.snackbar

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.di.SnackbarRoot
import com.theskillapp.skillapp.shared.extensions.getColorAttributeValue
import javax.inject.Inject
import javax.inject.Provider

class SnackbarShowerImpl @Inject constructor(
    @SnackbarRoot
    private val snackbarRootProvider: Provider<View?>,
) : SnackbarShower {
    override fun show(label: String, action: SnackbarShower.Action?) {
        val snackbarRoot = snackbarRootProvider.get() ?: return
        val context = snackbarRoot.context

        val snackbar = Snackbar
            .make(snackbarRoot, label, Snackbar.LENGTH_LONG)
            .setActionTextColor(context.getColorAttributeValue(R.attr.snackbarActionTextColor))

        if (action != null) {
            snackbar.setAction(action)
        }

        snackbar.show()
    }

    private fun Snackbar.setAction(action: SnackbarShower.Action) : Snackbar {
        return setAction(action.stringResId) { action.listener.invoke() }
    }
}
