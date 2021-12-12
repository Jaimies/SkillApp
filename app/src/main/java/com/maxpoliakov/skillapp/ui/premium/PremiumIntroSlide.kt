package com.maxpoliakov.skillapp.ui.premium

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.maxpoliakov.skillapp.databinding.PremiumIntroSlideBinding

class PremiumIntroSlide(
    private val titleResId: Int,
    private val descriptionResId: Int,
    private val videoFileName: String
) : Fragment() {
    lateinit var binding: PremiumIntroSlideBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PremiumIntroSlideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.videoView.run {
        binding.titleTextView.setText(titleResId)
        binding.descriptionTextView.setText(descriptionResId)

        if (videoFileName.isEmpty()) {
            isGone = true
            return@run
        }

        setOnPreparedListener { mp -> mp.isLooping = true }
        val rawId = resources.getIdentifier(videoFileName, "raw", requireContext().packageName)
        val uri = Uri.parse("android.resource://${requireContext().packageName}/$rawId")

        setVideoURI(uri)
        requestFocus()
        start()
    }

    override fun onResume() = binding.videoView.run {
        super.onResume()
        requestFocus()
        start()
    }
}
