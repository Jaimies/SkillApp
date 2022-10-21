package com.maxpoliakov.skillapp.ui.intro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.reflect.KClass

@ActivityScoped
class IntroUtilImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val activity: Activity,
): IntroUtil {
    private val intros = listOf(
        Intro(FirstRunIntro::class, "intro_viewed"),
        Intro(Intro_3_1_0::class, "new_intro_viewed"),
    )

    override fun showIntroIfNecessary() {
        intros
            .filterNot(Intro<*>::hasBeenShown)
            .first()
            .show()

        intros.markAllAsShown()
    }

    private fun List<Intro<*>>.markAllAsShown() {
        for (intro in this) intro.hasBeenShown = true
    }

    inner class Intro<T : Any>(
        val kClass: KClass<T>,
        val preferenceName: String,
    ) {
        var hasBeenShown: Boolean
            get() = sharedPreferences.getBoolean(preferenceName, false) && kClass != Intro_3_1_0::class
            set(value) = sharedPreferences.edit { putBoolean(preferenceName, value) }

        fun show() {
            val intent = Intent(activity, kClass.java)
            activity.startActivity(intent)
        }
    }
}
