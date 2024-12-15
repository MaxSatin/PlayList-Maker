package com.practicum.playlistmaker.medialibrary.ui.playlists_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.CreatePlaylistFragmentBinding

class CreatePlayListsFragment: Fragment() {

    private var _binding: CreatePlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private var playListName: String? = null
    private var playListDescription: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreatePlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}