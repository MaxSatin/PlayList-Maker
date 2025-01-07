package com.practicum.playlistmaker.medialibrary.ui.favorite_tracks_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.viewmodel.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.databinding.FavoriteTracksFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.screen_state.media_library.FavoriteListScreenState
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.player.ui.PlayerFragment

class FavoriteTracksFragment() : Fragment() {


    private val viewModel: FavoriteTracksViewModel by viewModel()
    private var _binding: FavoriteTracksFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoriteTracksAdapter

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

        viewModel.loadFavoriteTrackList()
        adapter = FavoriteTracksAdapter(viewModel::showTrackPlayer)

        binding.favoriteListRV.adapter = adapter
        binding.favoriteListRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,
            false)

        viewModel.getFavoriteListScreenState().observe(viewLifecycleOwner) { state ->
            renderState(state)
            Log.d("FavListState", "${state.toString()}")
        }

        viewModel.getShowTrackPlayerTrigger().observe(viewLifecycleOwner) { track ->
            showTrackPlayer(track)
        }
    }

    private fun renderState(state: FavoriteListScreenState) {
        when (state) {
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
        adapter.updateItems(favoriteTrackList)
        binding.progressBar.isVisible = false
        binding.favoriteListRV.isVisible = true
        binding.emptyLibraryPH.isVisible = false
    }

    private fun showEmpty(message: String) {
        binding.progressBar.isVisible = false
        binding.favoriteListRV.isVisible = false
        binding.emptyLibraryPH.isVisible = true
    }

    private fun showTrackPlayer(track: String) {
//        PlayerActivity.show(requireContext(), track)
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val FAVORITE_TRACKLIST = "favorite_track_list"

        fun newInstance(favoriteListScreenState: FavoriteListScreenState): FavoriteTracksFragment {
            TODO()
//            return FavoriteTracksFragment().apply {
//                arguments = bundleOf(FAVORITE_TRACKLIST to ArrayList(favoriteTracks))
//                Log.d("favoritesFragmentNewInstance", "$favoriteTracks")
        }
    }
}