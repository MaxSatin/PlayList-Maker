package com.practicum.playlistmaker.medialibrary.ui.playlists_fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistsFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlayListsScreenState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.createplaylists.viewmodel.CreatePlayListsViewModel
import com.practicum.playlistmaker.medialibrary.presentation.playlists.playlists.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.medialibrary.ui.decorations.GridLayoutItemDecorations
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment() : Fragment() {


    private val viewModel: PlaylistViewModel by viewModel()
    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!

    private val playlistAdapter = PlaylistAdapter {
        Log.d("Openplaylists", "playlist is open!")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylists()

        with(binding.playlistsRecyclerView) {
            adapter = playlistAdapter
            addItemDecoration(GridLayoutItemDecorations(2, 8, false))
        }

        viewModel.getPlayListScreenState().observe(viewLifecycleOwner) { playlistScreenState ->
            processState(playlistScreenState)
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            // получаем flatList отедельно и передаем в параметр getTrackList, чтобы использовать
//            // соответствующие методы для соответствующих версий Android
//            val favorites = emptyList<List<Track>>()
//            if (favorites.isNullOrEmpty()) {
//                binding.emptyPlayListsPH.isVisible = true
//            }
//        }

        binding.createPlayListButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlayListsFragment
            )
        }
    }

    private fun processState(state: PlayListsScreenState) {
        when (state) {
            is PlayListsScreenState.Content -> showContent(state.favoriteTrackList)
            else -> showEmptyPH()
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        playlistAdapter.updateItems(playlists)
        binding.playlistsRecyclerView.isVisible = true
    }

    private fun showEmptyPH() {
        binding.emptyPlayListsPH.isVisible = true
        binding.playlistsRecyclerView.isVisible = false
    }

//    private fun getTrackListList(
//        arguments: Bundle?,
//        flatList: ArrayList<Track>?,
//    ): List<List<Track>>? {
//        val sizes = arguments?.getIntegerArrayList(KEY_SIZES)
//
//        if (flatList == null || sizes == null) return null
//
//        val trackLists = ArrayList<List<Track>>()
//        var index = 0
//
//        sizes.forEach { size ->
//            val sublist = flatList.subList(index, index + size)
//            trackLists.add(sublist)
//            index += size
//        }
//        return trackLists
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val PLAY_LIST = "playlist"
        private const val KEY_TRACKS = "key_tracks"
        private const val KEY_SIZES = "key_sizes"


        fun newInstance(favoriteTracks: List<List<Track>>?): PlayListsFragment {
            val flatList = ArrayList<Track>()
            val sizes = ArrayList<Int>()

            favoriteTracks?.forEach { trackList ->
                sizes.add(trackList.size)

                flatList.addAll(trackList)
            }
            return PlayListsFragment().apply {
                arguments = bundleOf(
                    KEY_TRACKS to ArrayList(flatList),
                    KEY_SIZES to ArrayList(sizes)
                )
            }
        }
    }
}

