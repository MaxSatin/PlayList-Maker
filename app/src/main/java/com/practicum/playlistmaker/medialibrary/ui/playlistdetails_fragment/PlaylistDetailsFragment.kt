package com.practicum.playlistmaker.medialibrary.ui.playlistdetails_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.PlaylistDetailsFragmentBinding

class PlaylistDetailsFragment: Fragment() {

    private var _binding: PlaylistDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        private const val PLAYLIST_NAME_KEY = "playlistName"
        fun createArgs(playlistName: String) = bundleOf(PLAYLIST_NAME_KEY to playlistName)
    }
}