package com.practicum.playlistmaker.medialibrary.ui.playlist_details_fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistDetailsFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.NavigateFragment
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.PlaylistDetailsScreenState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.playlist_details.viewmodel.PlaylistDetailsViewModel
import com.practicum.playlistmaker.medialibrary.ui.edit_playlist_fragment.EditPlaylistDataFragment
import com.practicum.playlistmaker.player.ui.PlayerFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlaylistDetailsFragment : Fragment() {

    private var _binding: PlaylistDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistDetailsViewModel by viewModel()
    private val handler = Handler(Looper.getMainLooper())

    private var currentAction: Int = 0
    private var playListId: Long = 0
    private var isTrackListEmpty: Boolean = false
    private lateinit var trackId: String
    private var playlist: Playlist? = null

    private lateinit var trackListBHBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var editPLBHBehavior: BottomSheetBehavior<ConstraintLayout>

    private var confirmDialogPlaylistExists: MaterialAlertDialogBuilder? = null

    private lateinit var notificationFadeIn: Animation
    private lateinit var notificationFadeOut: Animation


    private var trackListAdapter: TrackListAdapter? = TrackListAdapter(
        onSingleTap = { track ->
            trackListBHBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.showTrackPlayer(track)
        },
        onLongPress = { track ->
            trackId = track.trackId
            trackNotificationFadeIn()
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = PlaylistDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationFadeIn =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        notificationFadeOut =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

        val playlistId = arguments?.getLong(PLAYLIST_NAME_KEY)
        if (playlistId != null) {
            playListId = playlistId
            viewModel.loadPlayListDetails(playlistId)
//            viewModel.loadPlaylistDetailsState(playlistName)
//            viewModel.getAllTracksFromPlaylist(playlistName)
        } else {
            Toast.makeText(requireContext(), "playlist is null", Toast.LENGTH_LONG).show()
        }
        Log.d("PlaylistName", "$playlistId")

        binding.trackListRV.adapter = trackListAdapter

        trackListBHBehavior = BottomSheetBehavior.from(binding.trackListBSContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        editPLBHBehavior = BottomSheetBehavior.from(binding.editPlayListBSContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.getPlayListDetailsMediatorLiveData()
            .observe(viewLifecycleOwner) { playListDetails ->
                Log.d("PlaylistDetails", "$playListDetails")
                processData(playListDetails)
            }

        viewModel.getShowFragmentLiveData().observe(viewLifecycleOwner) { parameter ->
            showFragment(parameter)

        }


        binding.moreIc.setOnClickListener {
            editPLBHBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.editEditPl.setOnClickListener {
            viewModel.showEditPlayListFragment(playListId)
        }
        currentAction = 0
        binding.toolbar.setOnClickListener {
            playlistNotificationFadeOut()
            trackNotificationFadeOut()
            closeBottomSheetAndNavigateBack()
        }

        binding.deleteButton.setOnClickListener {
            onDeleteTrackButtonPressed(playListId, trackId)
        }

        binding.cancel.setOnClickListener {
            onTracklistCancelButtonPressed()
        }

        binding.editDeletePl.setOnClickListener {
            playlistNotificationFadeIn()
        }

        binding.deletePlaylistButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onDeletePlaylistButtonPressed(playListId)
            }
        }

        binding.cancelPlaylistDeletion.setOnClickListener{
            onPlaylistCancelButtonPressed()
        }

        binding.shareIc.setOnClickListener{
           sharePlaylist()
        }

        binding.editShare.setOnClickListener{
            sharePlaylist()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    closeBottomSheetAndNavigateBack()
                }
            })

    }

    private fun showFragment(state: NavigateFragment) {
        when (state) {
            is NavigateFragment.PlayerFragment -> handler.postDelayed(
                {
                    findNavController().navigate(
                        R.id.action_playlistDetailsFragment_to_playerFragment,
                        PlayerFragment.createArgs(state.trackGson)
                    )
                },
                keyObject,
                300
            )

            is NavigateFragment.EditPlayListFragment -> handler.postDelayed(
                {
                    findNavController().navigate(
                        R.id.action_playlistDetailsFragment_to_editPlaylistDataFragment,
                        EditPlaylistDataFragment.createArgs(state.playListId)
                    )
                },
                keyObject,
                300
            )
        }
    }

    private fun sharePlaylist() {
        if (isTrackListEmpty){
            editPLBHBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showNothingToShareNote()
        } else {
            editPLBHBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.share()
        }
    }

    private fun closeBottomSheetAndNavigateBack() {
        if (trackListBHBehavior.state != BottomSheetBehavior.STATE_COLLAPSED && editPLBHBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            when (currentAction) {
                0 -> trackListBHBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                1 -> {
                    findNavController().navigate(
                        R.id.action_playlistDetailsFragment_to_mediaLibraryFragment
                    )
                }
            }
        } else if (trackListBHBehavior.state == BottomSheetBehavior.STATE_COLLAPSED && editPLBHBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            when (currentAction) {
                0 -> editPLBHBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                1 -> {
                    findNavController().navigate(
                        R.id.action_playlistDetailsFragment_to_mediaLibraryFragment
                    )
                }
            }
        } else if (trackListBHBehavior.state == BottomSheetBehavior.STATE_COLLAPSED && editPLBHBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            findNavController().navigate(
                R.id.action_playlistDetailsFragment_to_mediaLibraryFragment
            )
        }
        currentAction = (currentAction + 1) % 2

    }

    private fun processData(state: PlaylistDetailsScreenState) {
        when (state) {
            is PlaylistDetailsScreenState.Loading -> showLoading()
            is PlaylistDetailsScreenState.DetailsState -> {
                showPlaylistInfo(state)
                showTrackList(state.contents)
            }

            is PlaylistDetailsScreenState.Empty -> Toast.makeText(
                requireContext(),
                "Empty PL",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showPlaylistInfo(state: PlaylistDetailsScreenState.DetailsState) {
        binding.loadingScreen.isVisible = false
        if (state.playlist != null) {
            loadPlayListData(state.playlist)
            playListId = state.playlist.id
            this.playlist = state.playlist
            binding.tracksNumber.text = getTracksNumberAsTextField(state.playlist)
            binding.playlistDuration.text = getPlaylistDurationTextField(state.overallDuration)
            Log.d("OverAllDuration", "${state.overallDuration}")
        }
    }

    private fun showTrackList(trackList: List<Track>?) {
        if (!trackList.isNullOrEmpty()) {
            binding.trackListRV.isVisible = true
            binding.emptyPlaylistsPH.isVisible = false
            Log.d("TracklistAdapter", "${trackList}")
            trackListAdapter?.updateItems(trackList)
        } else {
            isTrackListEmpty = true
            binding.trackListRV.isVisible = false
            binding.emptyPlaylistsPH.isVisible = true
        }
    }

    private fun showNothingToShareNote() {
        binding.nothingToShareNotification.isVisible = true
        binding.nothingToShareNotification.startAnimation(notificationFadeIn)
        handler.postDelayed(
            {
                binding.nothingToShareNotification.startAnimation(notificationFadeOut)
                binding.nothingToShareNotification.isVisible = false
            },
            keyObject,
            ANIMATION_DELAY_MILLIS_1000
        )
    }

    private fun loadPlayListData(playList: Playlist) {
        upLoadImage(playList.coverUri, binding.poster)
        binding.playlistTitle.text = playList.name
        binding.playlistDescription.text = playList.description
        binding.tracksNumber.text = getTracksNumberAsTextField(playList)

        upLoadImage(playList.coverUri, binding.playListPreviewImage)
        binding.playListPreviewName.text = playList.name
        binding.playListPreviewTrackCount.text = getTracksNumberAsTextField(playList)
    }

    private fun upLoadImage(uri: Uri?, imageView: ImageView) {
        Glide.with(binding.root.context)
            .load(uri)
            .placeholder(R.drawable.vector_empty_album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(8))
            .into(imageView)

    }

    private fun takePersistableUriPermission(uri: Uri) {
        try {
            val contentResolver = requireActivity().contentResolver
            val flags =
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, flags)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun getTracksNumberAsTextField(playlist: Playlist): String {
        return String.format(
            Locale.getDefault(),
            "%d %s", playlist.trackCount,
            attachWordEndingTracks(playlist.trackCount)
        )
    }

    private fun getPlaylistDurationTextField(duration: Long): String {
        return String.format(
            Locale.getDefault(),
            "%d %s", duration,
            attachWordEndingMinutes(duration)
        )
    }

    private fun showLoading() {
        with(binding) {
            loadingScreen.isVisible = true
        }
    }

    private fun showEmptyPH() {
        binding.trackListRV.isVisible = false
        binding.emptyPlaylistsPH.isVisible = true
    }

    private fun trackNotificationFadeIn() {
        handler.removeCallbacksAndMessages(keyObject)
        handler.postDelayed(
            {
                binding.deleteTrackDialog.startAnimation(notificationFadeIn)
                binding.deleteTrackDialog.isVisible = true
            },
            keyObject,
            ANIMATION_DELAY_MILLIS
        )
    }

    private fun trackNotificationFadeOut() {
        handler.removeCallbacksAndMessages(keyObject)
        handler.postDelayed(
            {
                binding.deleteTrackDialog.startAnimation(notificationFadeOut)
                binding.deleteTrackDialog.isVisible = false
            },
            keyObject,
            ANIMATION_DELAY_MILLIS
        )
    }

    private fun playlistNotificationFadeIn() {
        handler.removeCallbacksAndMessages(keyObject)
        handler.postDelayed(
            {
                binding.deletePlaylistDialog.startAnimation(notificationFadeIn)
                binding.deletePlaylistDialog.isVisible = true
            },
            keyObject,
            ANIMATION_DELAY_MILLIS
        )
    }

    private fun playlistNotificationFadeOut() {
        handler.removeCallbacksAndMessages(keyObject)
        handler.postDelayed(
            {
                binding.deletePlaylistDialog.startAnimation(notificationFadeOut)
                binding.deletePlaylistDialog.isVisible = false
            },
            keyObject,
            200
        )
    }

    private fun onDeleteTrackButtonPressed(playListId: Long, trackId: String) {
        trackNotificationFadeOut()
        viewModel.deleteTrackFromPlaylist(playListId, trackId)
    }

    private suspend fun onDeletePlaylistButtonPressed(playListId: Long) {
        playlistNotificationFadeOut()
        delay(200)
        editPLBHBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        delay(200)
        viewModel.deletePlaylist(playListId)
        findNavController().navigate(
            R.id.action_playlistDetailsFragment_to_mediaLibraryFragment
        )
    }

    private fun onPlaylistCancelButtonPressed() {
        playlistNotificationFadeOut()
    }

    private fun onTracklistCancelButtonPressed() {
        trackNotificationFadeOut()
    }


    private fun attachWordEndingTracks(trackNumber: Int): String {
        return when {
            trackNumber % 10 == 0 -> "треков"
            trackNumber % 10 == 1 -> "трек"
            trackNumber % 10 in 2..4 -> "трека"
            trackNumber % 10 in 5..9 -> "треков"
            trackNumber % 100 in 11..19 -> "треков"
            else -> ""
        }
    }

    private fun attachWordEndingMinutes(minutesAmount: Long): String {
        return when {
            minutesAmount % 10 == 0L -> "минут"
            minutesAmount < 10 && minutesAmount % 10 == 1L -> "минута"
            minutesAmount < 10 && minutesAmount % 10 in 2..4 -> "минуты"
            minutesAmount % 10 in 2..4 -> "минуты"
            minutesAmount % 10 in 5..9 -> "минут"
            minutesAmount % 100 in 11..20 -> "минут"
            else -> ""
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlayListDetails(this.playListId)
    }

    override fun onDestroyView() {
        playlist = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        trackListAdapter = null
        super.onDestroy()
    }

    companion object {
        private val keyObject = Unit
        private const val PLAYLIST_NAME_KEY = "playlistName"
        private const val ANIMATION_DELAY_MILLIS = 300L
        private const val ANIMATION_DELAY_MILLIS_1000 = 1000L
        fun createArgs(playlistId: Long) = bundleOf(
            PLAYLIST_NAME_KEY to playlistId
        )
    }
}
