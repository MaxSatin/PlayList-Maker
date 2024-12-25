package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.os.trace
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.view.animation.AnimationUtils
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
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private var _binding: PlayerFragmentBinding? = null
    private val binding: PlayerFragmentBinding get() = _binding!!

    private lateinit var viewModel: PlayerViewModel
    private var isPrepared: Boolean = false
    private var isPreloaded: Boolean = false

    private lateinit var trackAddedNotificationFadeIn:Animation
    private lateinit var trackAddedNotificationFadeOut:Animation

    private val handler = Handler(Looper.getMainLooper())


    private val playlistAdapter = BottomSheetPlaylistAdapter { playlist: Playlist ->
//        viewModel.addTrackPlayListCrossRef(playlist.name)
        viewModel.addTrackToPlayList(playlist)
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
//        val trackGson = intent.getStringExtra(TRACK_ITEM_KEY)
        val trackGson = requireArguments().getString(TRACK_ITEM_KEY)
        viewModel = getViewModel { parametersOf(trackGson) }
        viewModel.getPlaylists()

        trackAddedNotificationFadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        trackAddedNotificationFadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

        binding.playlistsRV.adapter = playlistAdapter
        binding.playButton.isEnabled = false



        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.createPlayListButton.setOnClickListener{
            findNavController().navigate(
                R.id.action_playerFragment_to_createPlayListsFragment
            )
        }

        var currentAction = 0
        binding.playerButtonBack.setOnClickListener {
            if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN){
                when(currentAction){
                    0 -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    1 -> findNavController().navigateUp()
                }
            } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                findNavController().navigateUp()
            }
            currentAction = (currentAction + 1) % 2
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN){
                    when(currentAction){
                        0 -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        1 -> findNavController().navigateUp()
                    }
                } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                    findNavController().navigateUp()
                }
                currentAction = (currentAction + 1) % 2
            }
        })


        binding.addToTrackList.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.getCheckTrackBelongsToPlaylistLiveData().observe(viewLifecycleOwner){ state ->
            renderTrackState(state)
        }

        viewModel.getPlaylistStateLiveData().observe(viewLifecycleOwner) { state ->
            renderPlaylistState(state)
        }

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) { playerState ->
            renderPlayerState(playerState)


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
            viewModel.playerController()
        }

        binding.addToFavorites.setOnClickListener {
            viewModel.controlFavoriteState()
        }

    }

    private fun renderPlaylistState(state: PlayListsScreenState) {
        when (state){
            is PlayListsScreenState.Content -> {
                showPlayList(state.playlists)
            }
            is PlayListsScreenState.Empty -> showEmpty(state.message)
            else -> Toast.makeText(requireContext(), "Загрузка", Toast.LENGTH_LONG).show()

        }
    }

    private fun renderTrackState(state: TrackState){
        when (state){
            is TrackState.TrackInfo -> processTrackState(state)
        }
    }

    private fun processTrackState(state: TrackState.TrackInfo) {
        when {
            state.isAlreadyInPlaylist -> showTrackAddInPlaylistNotification(
                "Трек ${state.trackName} уже добавлен в плейлист ${state.playListName}"
            )
//                Toast.makeText(requireContext(),
//                "Трек уже есть в плейлисте!",
//                Toast.LENGTH_LONG).show()
            state.transactionId == -1L -> showTrackAddInPlaylistNotification(
                "Не удалось добавить трек ${state.trackName} в плейлист"
            )
//            Toast.makeText(requireContext(),
//                "Не удалось добавить трек в плейлист",
//                Toast.LENGTH_LONG).show()
            else ->
                showTrackAddInPlaylistNotification(
                    "Добавлено в плейлист ${state.playListName}"
                )
//                Toast.makeText(requireContext(),
//                "Трек добавлен в ${state.playListName} плейлист!",
//                Toast.LENGTH_LONG).show()
        }
    }

    private fun showTrackAddInPlaylistNotification(message:String) {
        binding.trackStateNotification.text = message
        binding.trackStateNotification.isVisible = true
        binding.trackStateNotification.startAnimation(trackAddedNotificationFadeIn)
        handler.postDelayed(
            {
                binding.trackStateNotification.startAnimation(trackAddedNotificationFadeOut)
                binding.trackStateNotification.isVisible = false
            },
            ANIMATION_DELAY
            )

    }

    private fun showPlayList(playList: List<Playlist>){
        playlistAdapter.updateItems(playList)
        binding.playlistsRV.isVisible = true
        binding.emptyPlaylistsPH.isVisible = false
    }

    private fun showEmpty(message: String){
        binding.emptyPlaylistsPH.isVisible = true
        binding.playlistsRV.isVisible = false
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_LONG)
    }

    private fun renderPlayerState(state: PlayerState) {
        when {
            state.isLoading -> showLoading()
            !isPreloaded -> showTrackDetails(state.track)
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
        binding.poster.isVisible = false
        binding.loadingOverlay.isVisible = true
    }

    private fun showTrackDetails(trackItem: TrackInfoModel) {
        binding.loadingOverlay.isVisible = false
        binding.progressbar.isVisible = false
        binding.poster.isVisible = true
        loadPoster(trackItem)
        binding.songName.text = trackItem.trackName
        binding.bandName.text = trackItem.artistName
        binding.timePlayed.text = "00:30"
        binding.tracklengthTime.text = trackItem.trackTimeMillis
        binding.albumTitle.text = trackItem.collectionName
        binding.albumYear.text = trackItem.releaseDate
        binding.trackGenre.text = trackItem.primaryGenreName
        binding.trackCountry.text = trackItem.country
        handleIsFavoriteStatus(trackItem.isInFavorite)
        isPreloaded = true
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
        super.onDestroyView()
        _binding = null
        viewModel.releasePlayer()
    }

    companion object {
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val ANIMATION_DELAY = 1_500L

        fun createArgs(trackGson: String): Bundle = bundleOf(TRACK_ITEM_KEY to trackGson)

    }
}