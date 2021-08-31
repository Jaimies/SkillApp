package com.maxpoliakov.skillapp.ui.premium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maxpoliakov.skillapp.databinding.PremiumFragBinding

class PremiumFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = PremiumFragBinding.inflate(inflater, container, false)
        return binding.root
    }
}
