package com.practicum.playlistmaker.medialibrary.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.PlaylistsFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.PlayListsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment(): Fragment() {
    companion object {

        private const val FAVORITE_TRACKLIST = "playlist"

        fun newInstance(favoriteTracks: List<List<Track>>): FavoriteTracksFragment {
            return FavoriteTracksFragment().apply {
                arguments = bundleOf(FAVORITE_TRACKLIST to ArrayList(favoriteTracks))
            }
        }
    }

    private val viewModel: PlayListsViewModel by viewModel()
    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playLists: ArrayList<List<Track>>? = arguments?.getSerializable(FAVORITE_TRACKLIST, ArrayList::class.java) as? ArrayList<List<Track>>
        if (playLists.isNullOrEmpty()){
            TODO()
        }
    }
}
