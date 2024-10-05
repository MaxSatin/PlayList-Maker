package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.Creator.GsonProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.player.presentation.mapper.DateFormatter
import com.practicum.playlistmaker.player.presentation.model.TrackInfoModel
import com.practicum.playlistmaker.player.presentation.state.PlayStatus
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import com.practicum.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.search.ui.SearchActivity.Companion


class PlayerActivity : AppCompatActivity() {

//    private val handler = Handler(Looper.getMainLooper())
//    private val gson = GsonProvider.gson

//    private val mediaPlayer = Creator.provideMediaPlayerInteractor()
//
//    lateinit var trackItem: Track

    //    private var runnable: Runnable? = null
    lateinit var binding: ActivityPlayerBinding
    lateinit var viewModel: PlayerViewModel
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

//        trackItem = obtainTrackInstance()
//        showTrackDetails(trackItem)
//        loadPoster(trackItem)

        viewModel.getScreenStateLiveData().observe(this) { playerState ->
            render(playerState)
        }

        viewModel.getPlayerPreparedStatusLiveData().observe(this) { isPrepared ->
            if (isPrepared == true) {
                binding.stopPlayerButton.isEnabled = true
                this.isPrepared = true
            }
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changePlayButtonStyle(playStatus)
            binding.timePlayed.text = playStatus.progress
        }
//        viewModel.preparePlayer()
//        mediaPlayer.preparePlayer(trackItem.previewUrl)
        binding.stopPlayerButton.setOnClickListener {
            viewModel.playerController()
//            if (!mediaPlayer.isPlaying()) {
//                startPlayer()
//            } else {
//                pausePlayer()
//            }
        }
    }

//    private fun obtainTrackInstance(): Track {
//        val intent = intent
//        val trackItemGson = intent.getStringExtra(TRACK_ITEM_KEY)
//        val trackItem = gson.fromJson<Track>(trackItemGson, Track::class.java)
//        return trackItem
//    }

    private fun render(state: PlayerState) {
        when (state) {
            is PlayerState.Loading -> showLoading()
            is PlayerState.Content -> showTrackDetails(state.track)
        }
    }


//    private fun startPlayer() {
//        mediaPlayer.playerStart()
//        showTimeCountDown()
//        binding.stopPlayerButton.isChecked = true
//    }
//
//    private fun pausePlayer() {
//        mediaPlayer.playerPause()
//        val currentRunnable = runnable
//        if (currentRunnable != null) {
//            handler.removeCallbacks(currentRunnable)
//        }
//        binding.stopPlayerButton.isChecked = false
//    }
//
//    private fun showTimeCountDown() {
//        val currentRunnable = runnable
//        if (currentRunnable != null) {
//            handler.removeCallbacks(currentRunnable)
//        }
//        val newTimerRunnable = object : Runnable {
//            override fun run() {
//                binding.timePlayed.text =
//                    DateFormatter.timeFormatter.format(mediaPlayer.getCurrentPosition())
//                handler.postDelayed(this, TIMER_DELAY)
//            }
//        }
//
//        this.runnable = newTimerRunnable
//
//        handler.postDelayed(newTimerRunnable, TIMER_DELAY)
//
//        mediaPlayer.setOnCompleteListener {
//            handler.removeCallbacks(newTimerRunnable)
//            binding.timePlayed.text = DateFormatter.timeFormatter.format(0)
//            binding.stopPlayerButton.isChecked = false
//        }
//    }

    private fun changePlayButtonStyle(state: PlayStatus) {
        when (state.isPlaying) {
            true -> binding.stopPlayerButton.isChecked = true
            false -> binding.stopPlayerButton.isChecked = false
        }
    }

    private fun showLoading() {
        binding.poster.isVisible = false
        binding.progressbar.isVisible = true
    }

    private fun showTrackDetails(trackItem: TrackInfoModel) {
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

//        override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer.releasePlayer()
//        val currentRunnable = runnable
//        if (currentRunnable != null) {
//            handler.removeCallbacks(currentRunnable)
//        }
//    }

    override fun onPause() {
        super.onPause()
        binding.stopPlayerButton.isChecked = false
        viewModel.pausePlayer()

    }

    companion object {
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val TIMER_DELAY = 50L

        fun show(context: Context, trackGson: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK_ITEM_KEY, trackGson)
            context.startActivity(intent)
        }

    }

}