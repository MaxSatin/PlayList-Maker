package com.practicum.playlistmaker.medialibrary.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.databinding.FavoriteTracksFragmentBinding

class FavoriteTracksFragment() : Fragment() {

    companion object {

        private const val FAVORITE_TRACKLIST = "favorite_track_list"

        fun newInstance(favoriteTracks: List<Track>?): FavoriteTracksFragment {
            return FavoriteTracksFragment().apply {
                arguments = bundleOf(FAVORITE_TRACKLIST to favoriteTracks)
            }
        }
    }

    private val viewModel: FavoriteTracksViewModel by viewModel()
    private var _binding: FavoriteTracksFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FavoriteTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val favorites =
                arguments?.getParcelable(FAVORITE_TRACKLIST, ArrayList::class.java) as List<Track>
            if (favorites.isNullOrEmpty()) {
                binding.emptyLibraryPH.isVisible = true
            }
        }
        else {
            val favorites = arguments?.getParcelableArrayList<Track>(FAVORITE_TRACKLIST)
            if (favorites.isNullOrEmpty()) {
                if (favorites.isNullOrEmpty()) {
                    binding.emptyLibraryPH.isVisible = true
                }
            }
        }
    }
}