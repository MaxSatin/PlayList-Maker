package com.practicum.playlistmaker.medialibrary.ui.playlists_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistsFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.media_library.PlayListsScreenState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.playlists.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.medialibrary.ui.decorations.GridLayoutItemDecorations
import com.practicum.playlistmaker.medialibrary.ui.playlist_details_fragment.PlaylistDetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment() : Fragment() {


    private val viewModel: PlaylistViewModel by viewModel()
    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())

    private var latestAddedPlaylist: Playlist? = null

    private var lastPostedList = emptyList<Playlist>()

    private lateinit var playlistAddedNotificationFadeIn: Animation
    private lateinit var playlistAddedNotificationFadeOut: Animation

    private var isFirstTimeLoaded: Boolean = true

    private val playlistAdapter = PlaylistAdapter { playlist ->

        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playlistDetailsFragment,
            PlaylistDetailsFragment.createArgs(playlist.id)
        )
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

        playlistAddedNotificationFadeIn =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        playlistAddedNotificationFadeOut =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

        viewModel.getPlaylists()

        with(binding.playlistsRecyclerView) {
            adapter = playlistAdapter
            addItemDecoration(GridLayoutItemDecorations(2, 8, true))
        }


        viewModel.playlistStateMediatorLiveData()
            .observe(viewLifecycleOwner) { playlistScreenState ->
                Log.d("PlaylistState", "$playlistScreenState")
                processState(playlistScreenState)
            }

        if (!arguments?.getString(PLAYLIST_CREATED).isNullOrEmpty()) {
            var playlistName: String? = arguments?.getString(PLAYLIST_CREATED)
            Log.d("PlaylistArgs", "${arguments?.getString(PLAYLIST_CREATED)}")
            showAddedInPlaylistNotification("Плейлист $playlistName создан!")
        }


        binding.createPlayListButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlayListsFragment
            )
        }
    }

    private fun <T> areListsAreEqual(
        list1: List<T>,
        list2: List<T>,
        comparator: (T, T) -> Boolean,
    ): Boolean {
        if (list1.size != list2.size) return false
        return list1.zip(list2).all { (item1, item2) -> comparator(item1, item2) }
    }

    private fun processState(state: PlayListsScreenState) {
        when (state) {
            is PlayListsScreenState.Content -> showContent(state.playlists)
            else -> showEmptyPH()
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        val playlist = playlists.firstOrNull()
        if (isFirstTimeLoaded) {
            isFirstTimeLoaded = false
        } else {
            var areListsEqual = areListsAreEqual(lastPostedList, playlists) { p1, p2 ->
                p1.name == p2.name
            }
            if (!areListsEqual) {
                lastPostedList = playlists
                showAddedInPlaylistNotification("Плейлист ${playlist?.name} создан!")
            }
        }

        playlistAdapter.updateItems(playlists)
        binding.playlistsRecyclerView.isVisible = true
    }

    private fun showEmptyPH() {
        binding.emptyPlayListsPH.isVisible = true
        binding.playlistsRecyclerView.isVisible = false
    }

    private fun showAddedInPlaylistNotification(playlistName: String) {
        binding.playlistCreatedNotification.text = playlistName
        binding.playlistCreatedNotification.isVisible = true
        binding.playlistCreatedNotification.startAnimation(playlistAddedNotificationFadeIn)
        handler.postDelayed(
            {
                binding.playlistCreatedNotification.startAnimation(playlistAddedNotificationFadeOut)
                binding.playlistCreatedNotification.isVisible = false
            },
            keyObject,
            ANIMATION_DELAY
        )

    }

    override fun onDestroyView() {
        isFirstTimeLoaded = false
        handler.removeCallbacksAndMessages(keyObject)
        latestAddedPlaylist = null
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val PLAYLIST_CREATED = "playlist"
        private val keyObject: Any = Unit
        private const val ANIMATION_DELAY = 1_500L

        fun createArgs(playlistName: String): Bundle = bundleOf(
            PLAYLIST_CREATED to playlistName,
        )
    }
}



