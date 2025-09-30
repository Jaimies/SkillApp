package com.theskillapp.skillapp.shared.snackbar

interface SnackbarShower {
    fun show(label: String, action: Action? = null)

    data class Action(val stringResId: Int, val listener: () -> Unit)
}
