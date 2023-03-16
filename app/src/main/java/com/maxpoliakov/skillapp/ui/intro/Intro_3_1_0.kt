package com.maxpoliakov.skillapp.ui.intro

import android.app.Activity
import androidx.appcompat.widget.Toolbar
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.extensions.getViewByIdWhenReady
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class Intro_3_1_0 @Inject constructor(
    private val activity: Activity,
) {
    suspend fun show(toolbar: Toolbar) {
        val tapTarget = TapTarget.forView(
            toolbar.getViewByIdWhenReady(R.id.edit),
            activity.getString(R.string.intro_3_1_0_title),
            activity.getString(R.string.intro_3_1_0_text)
        ).tintTarget(false)

        TapTargetView.showFor(activity, tapTarget)
    }
}
