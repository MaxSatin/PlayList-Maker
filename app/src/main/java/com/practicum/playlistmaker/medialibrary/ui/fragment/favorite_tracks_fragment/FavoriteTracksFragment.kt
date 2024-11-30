package com.practicum.playlistmaker.medialibrary.ui.fragment.favorite_tracks_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.databinding.FavoriteTracksFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.screen_state.FavoriteListScreenState
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track

class FavoriteTracksFragment() : Fragment() {

    companion object {

        private const val FAVORITE_TRACKLIST = "favorite_track_list"

        fun newInstance(favoriteListScreenState: FavoriteListScreenState): FavoriteTracksFragment {
            TODO()
//            return FavoriteTracksFragment().apply {
//                arguments = bundleOf(FAVORITE_TRACKLIST to ArrayList(favoriteTracks))
//                Log.d("favoritesFragmentNewInstance", "$favoriteTracks")
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

        viewModel.getFavoriteListScreenState().observe(viewLifecycleOwner){ state ->
            renderState(state)
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val favorites =
//                arguments?.getParcelable(FAVORITE_TRACKLIST, ArrayList::class.java) as List<Track>
//            if (favorites.isNullOrEmpty()) {
//                binding.emptyLibraryPH.isVisible = true
//            }
//        }
//        else {
//            val favorites = arguments?.getParcelableArrayList<Track>(FAVORITE_TRACKLIST)
//            Log.d("favoritevalue", "$favorites")
//            if (favorites.isNullOrEmpty()) {
//                if (favorites.isNullOrEmpty()) {
//                    binding.emptyLibraryPH.isVisible = true
//                }
            }

    private fun renderState(state: FavoriteListScreenState) {
        when(state){
            is FavoriteListScreenState.Loading -> showLoading()
            is FavoriteListScreenState.Content -> showContent(state.favoriteTrackList)
            is FavoriteListScreenState.Empty -> showEmpty(state.message)
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.favoriteListRV.isVisible = false
        binding.emptyLibraryPH.isVisible = false
    }

    private fun showContent(favoriteTrackList: List<Track>) {

        binding.progressBar.isVisible = true
        binding.favoriteListRV.isVisible = false
        binding.emptyLibraryPH.isVisible = false
    }

    private fun showEmpty(message: String) {
        binding.progressBar.isVisible = false
        binding.favoriteListRV.isVisible = false
        binding.emptyLibraryPH.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}