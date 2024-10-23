package com.practicum.playlistmaker.medialibrary.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.PlaylistsFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.PlayListsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment() : Fragment() {
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
                arguments = bundleOf(KEY_TRACKS to flatList)
                arguments = bundleOf(KEY_SIZES to sizes)
            }
//            val arrayList = ArrayList<List<Track>>()
//            favoriteTracks?.forEach { trackList ->
//                arrayList.add(ArrayList(trackList))
//            }
//            return PlayListsFragment().apply {
//
//                arguments = bundleOf(PLAY_LIST to arrayList)
//            }
        }
    }

    private val viewModel: PlayListsViewModel by viewModel()
    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val favorites =
                arguments?.getParcelable(PLAY_LIST, ArrayList::class.java) as List<List<Track>>
            if (favorites.isNullOrEmpty()) {
                binding.emptyPlayListsPH.isVisible = true
            }
        }
        else {
            val favorites = getTrackListList(arguments)
            if (favorites.isNullOrEmpty()) {
                if (favorites.isNullOrEmpty()) {
                    binding.emptyPlayListsPH.isVisible = true
                }
            }
        }
    }

    private fun getTrackListList(arguments: Bundle?): List<List<Track>>? {
        val flatList = arguments?.getParcelableArrayList<Track>(KEY_TRACKS)
        val sizes = arguments?.getIntegerArrayList(KEY_SIZES)

        if (flatList == null || sizes == null) return null

        val trackLists = ArrayList<List<Track>>()
        var index = 0

        sizes.forEach { size ->
            val sublist = flatList.subList(index, index + size)
            trackLists.add(sublist)
            index += size
        }
        return trackLists
    }
}

