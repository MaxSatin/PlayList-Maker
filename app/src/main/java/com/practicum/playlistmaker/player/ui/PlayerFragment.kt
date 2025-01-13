package com.practicum.playlistmaker.player.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlayerFragmentBinding
import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.player.domain.model.track_model.TrackInfoModel
import com.practicum.playlistmaker.player.presentation.state.PlayListsScreenState
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import com.practicum.playlistmaker.player.presentation.state.TrackState
import com.practicum.playlistmaker.player.presentation.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private var _binding: PlayerFragmentBinding? = null
    private val binding: PlayerFragmentBinding get() = _binding!!

//    private val viewModel: PlayerViewModel by viewModel()

    private lateinit var viewModel: PlayerViewModel
    private var isPlayerStarted: Boolean = false
    private var isPrepared: Boolean = false
    private var isPreloaded: Boolean = false

    private lateinit var trackAddedNotificationFadeIn: Animation
    private lateinit var trackAddedNotificationFadeOut: Animation

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val handler = Handler(Looper.getMainLooper())

    private val playlistAdapter = BottomSheetPlaylistAdapter { playlist: Playlist ->
        viewModel.addTrackToPlayList(playlist)
        handler.postDelayed(
            {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            },
            keyObject,
            300
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = PlayerFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackGson = requireArguments().getString(TRACK_ITEM_KEY)
        viewModel = getViewModel { parametersOf(trackGson) }
//        viewModel.setTrackGson(trackGson)

        trackAddedNotificationFadeIn =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        trackAddedNotificationFadeOut =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

//        if (!isPlayerStarted) {
//            viewModel.preparePlayer()
//        }

        binding.playlistsRV.adapter = playlistAdapter
        binding.playButton.isEnabled = false

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.createPlayListButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            handler.postDelayed(
                {
                    findNavController().navigate(R.id.action_playerFragment_to_createPlayListsFragment)
                },
                keyObject,
                300
            )
        }

        var currentAction = 0
        binding.playerButtonBack.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                when (currentAction) {
                    0 -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    1 -> findNavController().navigateUp()
                }
            } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                findNavController().navigateUp()
            }
            currentAction = (currentAction + 1) % 2
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                        when (currentAction) {
                            0 -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                            1 -> findNavController().navigateUp()
                        }
                    } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                        findNavController().navigateUp()
                    }
                    currentAction = (currentAction + 1) % 2
                }
            })


        binding.addToTrackList.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.getCheckTrackBelongsToPlaylistLiveData().observe(viewLifecycleOwner) { state ->
            renderTrackState(state)
        }

        viewModel.getPlaylistStateLiveData().observe(viewLifecycleOwner) { state ->
            renderPlaylistState(state)
        }

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) { playerState ->
            Log.d("PlayerState", "playerState: $playerState")
            renderPlayerState(playerState)

            Log.d("PlayerState", "is prepared: $isPrepared, ispreloaded: $isPreloaded")
            if (playerState.playStatus.isPrepared) {
                binding.playButton.isEnabled = true
                this.isPrepared = true
            }

            changePlayButtonStyle(playerState.playStatus.isPlaying)

            if (playerState.playStatus.isPlaying) {
                binding.timePlayed.text = playerState.playStatus.progress
            }
        }

        binding.playButton.setOnClickListener {
            isPlayerStarted = true
            viewModel.playerController()
        }

        binding.addToFavorites.setOnClickListener {
            viewModel.controlFavoriteState()
        }

    }

    private fun renderPlaylistState(state: PlayListsScreenState) {
        when (state) {
            is PlayListsScreenState.Content -> {
                showPlayList(state.playlists)
            }

            is PlayListsScreenState.Empty -> showEmpty(state.message)
            else -> Toast.makeText(requireContext(), "Загрузка", Toast.LENGTH_LONG).show()

        }
    }

    private fun renderTrackState(state: TrackState) {
        when (state) {
            is TrackState.TrackInfo -> processTrackState(state)
        }
    }

    private fun processTrackState(state: TrackState.TrackInfo) {
        when {
            state.isAlreadyInPlaylist -> showAddedInPlaylistNotification(
                "Трек ${state.trackName} уже добавлен в плейлист ${state.playListName}"
            )
            state.transactionId == -1L -> showAddedInPlaylistNotification(
                "Не удалось добавить трек ${state.trackName} в плейлист"
            )

            else ->
                showAddedInPlaylistNotification(
                    "Добавлено в плейлист ${state.playListName}"
                )

        }
    }

    private fun showAddedInPlaylistNotification(message: String) {
        binding.trackStateNotification.text = message
        binding.trackStateNotification.isVisible = true
        binding.trackStateNotification.startAnimation(trackAddedNotificationFadeIn)
        handler.postDelayed(
            {
                binding.trackStateNotification.startAnimation(trackAddedNotificationFadeOut)
                binding.trackStateNotification.isVisible = false
            },
            keyObject,
            ANIMATION_DELAY
        )

    }

    private fun showPlayList(playList: List<Playlist>) {
        playlistAdapter.updateItems(playList)
        binding.playlistsRV.isVisible = true
        binding.emptyPlaylistsPH.isVisible = false
    }

    private fun showEmpty(message: String) {
        binding.emptyPlaylistsPH.isVisible = true
        binding.playlistsRV.isVisible = false
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_LONG)
    }

    private fun renderPlayerState(state: PlayerState) {
        when {
            state.isLoading -> showLoading()
            !isPreloaded -> showTrackDetails(state)
        }
    }

    private fun changePlayButtonStyle(isPlaying: Boolean) {
        when (isPlaying) {
            true -> binding.playButton.isChecked = true
            false -> binding.playButton.isChecked = false
        }
    }

    private fun handleIsFavoriteStatus(isInFavorite: Boolean) {
        binding.addToFavorites.isChecked = isInFavorite
    }

    // Скрываем poster, т.к. к расположению poster подвязаны остальные элементы дизайна.
    // Скрывая poster - скрываем и остальные элементы. (наверно, кривое решение. буду очень рад комметариям!))
    private fun showLoading() {
        binding.poster.isVisible = true
        binding.progressbar.isVisible = true
    }

    private fun showTrackDetails(playerState: PlayerState) {
        with(playerState) {
            binding.progressbar.isVisible = false
            binding.progressbar.isVisible = false
            binding.poster.isVisible = true
            loadPoster(track)
            binding.songName.text = track.trackName
            binding.bandName.text = track.artistName
            binding.timePlayed.text = playStatus.progress
            binding.tracklengthTime.text = track.trackTimeMillis
            binding.albumTitle.text = track.collectionName
            binding.albumYear.text = track.releaseDate
            binding.trackGenre.text = track.primaryGenreName
            binding.trackCountry.text = track.country
            handleIsFavoriteStatus(track.isInFavorite)
            isPreloaded = true
        }
    }

    private fun loadPoster(trackItem: TrackInfoModel) {
        Glide.with(binding.root.context)
            .load(trackItem.getCoverArtWork())
            .placeholder(R.drawable.vector_empty_album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.small_corner_radius)))
            .into(binding.poster)
    }

    override fun onPause() {
        super.onPause()
        binding.playButton.isChecked = false
        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(keyObject)
        super.onDestroyView()
        _binding = null
        isPreloaded = false
        isPrepared = false
    }

    override fun onDestroy() {
        viewModel.releasePlayer()
        super.onDestroy()
    }

    companion object {
        private val keyObject: Any = Unit
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val ANIMATION_DELAY = 1_500L

        fun createArgs(trackGson: String): Bundle = bundleOf(TRACK_ITEM_KEY to trackGson)

    }
}