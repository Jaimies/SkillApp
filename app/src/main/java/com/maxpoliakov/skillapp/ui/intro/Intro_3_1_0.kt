package com.maxpoliakov.skillapp.ui.intro

import android.os.Bundle
import com.maxpoliakov.skillapp.R

class Intro_3_1_0 : BaseIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(R.string.new_intro_title, R.string.new_intro_text)
    }
}
