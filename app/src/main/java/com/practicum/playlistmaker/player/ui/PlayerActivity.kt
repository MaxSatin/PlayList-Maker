package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.presentation.model.TrackInfoModel
import com.practicum.playlistmaker.player.presentation.state.PlayStatus
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import com.practicum.playlistmaker.player.presentation.view_model.PlayerViewModel


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private var isPrepared: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.stopPlayerButton.isEnabled = false

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel
                .getPlayerViewModelFactory(intent.getStringExtra(TRACK_ITEM_KEY))
        )[PlayerViewModel::class.java]

        binding.playerButtonBack.setOnClickListener {
            finish()
        }

        viewModel.getPlayerStateLiveData().observe(this) { playerState ->
            render(playerState)

            if (playerState.playStatus.isPrepared) {
                binding.stopPlayerButton.isEnabled = true
                this.isPrepared = true
            }

            changePlayButtonStyle(playerState.playStatus.isPlaying)

            if (playerState.playStatus.isPlaying) {
                binding.timePlayed.text = playerState.playStatus.progress
            }
        }
//
//        viewModel.getPlayerStateLiveData().observe(this) { playerState ->
//            render(playerState)
//        }
//
//        viewModel.getPlayerPreparedStatusLiveData().observe(this) { isPrepared ->
//            if (isPrepared == true) {
//                binding.stopPlayerButton.isEnabled = true
//                this.isPrepared = true
//            }
//        }
//
//        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
//            changePlayButtonStyle(playStatus)
//            binding.timePlayed.text = playStatus.progress
//        }

        binding.stopPlayerButton.setOnClickListener {
            viewModel.playerController()
        }
    }

    private fun render(state: PlayerState) {
        if (state.isLoading) {
            showLoading()
        } else {
            showTrackDetails(state.track)
        }
//        when (state) {
//            is PlayerState.Loading ->
//            is PlayerState.Content -> showTrackDetails(state.track)
//        }
    }


    //    private fun changePlayButtonStyle(state: PlayStatus) {
//        when (state.isPlaying) {
    private fun changePlayButtonStyle(isPlaying: Boolean) {
        when (isPlaying) {
            true -> binding.stopPlayerButton.isChecked = true
            false -> binding.stopPlayerButton.isChecked = false
        }
    }

    private fun showLoading() {
        binding.loadingOverlay.isVisible = true
//        binding.poster.isVisible = false
//        binding.progressbar.isVisible = true
    }

    private fun showTrackDetails(trackItem: TrackInfoModel) {
        binding.loadingOverlay.isVisible = false
        binding.poster.isVisible = true
        binding.progressbar.isVisible = false
        loadPoster(trackItem)
        binding.songName.text = trackItem.trackName
        binding.bandName.text = trackItem.artistName
        binding.timePlayed.text = "0:30"
        binding.tracklengthTime.text = trackItem.trackTimeMillis
        binding.albumTitle.text = trackItem.collectionName
        binding.albumYear.text = trackItem.releaseDate
        binding.trackGenre.text = trackItem.primaryGenreName
        binding.trackCountry.text = trackItem.country
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
        binding.stopPlayerButton.isChecked = false
        viewModel.pausePlayer()

    }

    companion object {
        private const val TRACK_ITEM_KEY = "trackItem"

        fun show(context: Context, trackGson: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK_ITEM_KEY, trackGson)
            context.startActivity(intent)
        }

    }

}