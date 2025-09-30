package com.theskillapp.skillapp.ui.intro

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.theskillapp.skillapp.databinding.IntroSlideBinding

class IntroSlide : Fragment() {
    lateinit var binding: IntroSlideBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = IntroSlideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.videoView.run {
        val titleResId = requireArguments().getInt(ARG_TITLE_RES_ID)
        val descriptionResId = requireArguments().getInt(ARG_DESCRIPTION_RES_ID)
        val videoFileName = requireArguments().getString(ARG_VIDEO_FILE_NAME)

        binding.titleTextView.setText(titleResId)
        binding.descriptionTextView.setText(descriptionResId)

        if (videoFileName.isNullOrEmpty()) {
            isGone = true
            return@run
        }

        setOnPreparedListener { mp ->
            mp.isLooping = true
            mp.setVolume(0f, 0f)
        }

        val rawId = resources.getIdentifier(videoFileName, "raw", requireContext().packageName)
        val uri = Uri.parse("android.resource://${requireContext().packageName}/$rawId")

        setVideoURI(uri)
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.videoView.requestFocus()
        binding.videoView.start()
    }

    companion object {
        fun newInstance(titleResId: Int, descriptionResId: Int, videoFileName: String): IntroSlide {
            val fragment = IntroSlide()
            fragment.arguments = Bundle().apply {
                putInt(ARG_TITLE_RES_ID, titleResId)
                putInt(ARG_DESCRIPTION_RES_ID, descriptionResId)
                putString(ARG_VIDEO_FILE_NAME, videoFileName)
            }
            return fragment
        }

        private const val ARG_TITLE_RES_ID = "ARG_TITLE_RES_ID"
        private const val ARG_DESCRIPTION_RES_ID = "ARG_DESCRIPTION_RES_ID"
        private const val ARG_VIDEO_FILE_NAME = "ARG_VIDEO_FILE_NAME"
    }
}
