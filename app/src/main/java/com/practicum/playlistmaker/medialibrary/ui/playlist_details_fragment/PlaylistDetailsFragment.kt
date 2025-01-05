package com.practicum.playlistmaker.medialibrary.ui.playlist_details_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistDetailsFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlaylistDetailsScreenState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.playlist_details.viewmodel.PlaylistDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlaylistDetailsFragment: Fragment() {

    private var _binding: PlaylistDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistName = arguments?.getString(PLAYLIST_NAME_KEY)
        Log.d("PlaylistName", "$playlistName")

        if (playlistName != null) {
            viewModel.loadPlaylistDetails(playlistName)
        } else {
            Toast.makeText(requireContext(), "playlist is null", Toast.LENGTH_LONG).show()
        }

        viewModel.getPlaylistDetailsLiveData().observe(viewLifecycleOwner){ playlistState ->
            processData(playlistState)
        }

    }

    private fun processData(state:PlaylistDetailsScreenState){
        when {
            state.playlist != null -> showPlaylistInfo(state)
        }
    }

    private fun showPlaylistInfo(state: PlaylistDetailsScreenState) {
        with(state.playlist!!) {
            Glide.with(binding.root.context)
                .load(coverUri)
                .placeholder(R.drawable.vector_empty_album_placeholder)
                .fitCenter()
                .transform(RoundedCorners(8))
                .into(binding.poster)
            binding.playlistTitle.text = name
            binding.playlistDescription.text = description
        }
        binding.playlistDuration.text = String.format(
            Locale.getDefault(),
            "%d %s", state.overallDuration,
            attachWordEndingMinutes(state.overallDuration)
        )
        binding.tracksNumber.text = String.format(Locale.getDefault(),
            "%d %s", state.playlist.trackCount,
            attachWordEndingTracks(state.contents.size)
        )
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
            minutesAmount % 10 == 1L -> "минута"
            minutesAmount % 10 in 2..4 -> "минуты"
            minutesAmount % 10 in 5..9 -> "минут"
            minutesAmount % 100 in 11..20 -> "минут"
            else -> ""
        }
    }



    companion object {

        private const val PLAYLIST_NAME_KEY = "playlistName"
        fun createArgs(playlistName: String) = bundleOf(
            PLAYLIST_NAME_KEY to playlistName
        )
    }
}
