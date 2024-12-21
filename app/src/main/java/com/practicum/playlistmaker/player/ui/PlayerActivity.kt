package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.presentation.model.TrackInfoModel
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import com.practicum.playlistmaker.player.presentation.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class PlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private var isPrepared: Boolean = false
    private var isPreloaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val trackGson = intent.getStringExtra(TRACK_ITEM_KEY)
        viewModel = getViewModel { parametersOf(trackGson) }
        binding.playButton.isEnabled = false

        binding.playerButtonBack.setOnClickListener {
            finish()
        }

        viewModel.getPlayerStateLiveData().observe(this) { playerState ->
            render(playerState)

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

    private fun render(state: PlayerState) {
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

    companion object {
        private const val TRACK_ITEM_KEY = "trackItem"

        fun createArgs(trackGson: String): Bundle = bundleOf(TRACK_ITEM_KEY to trackGson)


        fun show(context: Context, trackGson: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK_ITEM_KEY, trackGson)
            context.startActivity(intent)
        }

    }

}