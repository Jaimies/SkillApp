package com.maxpoliakov.skillapp.ui.intro

import android.os.Bundle
import com.maxpoliakov.skillapp.R
import java.time.LocalDate
import java.time.Month

class FirstRunIntro : BaseIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(R.string.intro_slide1_title, R.string.intro_slide1_description, "intro_reorder")
        addSlide(R.string.intro_slide2_title, R.string.intro_slide2_description, "intro_grouping")
        addSlide(R.string.intro_slide3_title, R.string.intro_slide3_description, "intro_units")
        addSlide(R.string.intro_slide4_title, R.string.intro_slide4_description, "intro_backups")
    }
}
