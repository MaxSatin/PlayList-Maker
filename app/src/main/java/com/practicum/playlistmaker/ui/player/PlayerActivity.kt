package com.practicum.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.Creator.GsonProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.mapper.DateFormatter

//import com.practicum.playlistmaker.ui.player.MediaPlayerController.Companion.STATE_PREPARED
//import com.practicum.playlistmaker.ui.player.MediaPlayerController.Companion.TIMER_DELAY

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val TIMER_DELAY = 50L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val gson = GsonProvider.gson

    //    private val gson = Gson()
    private val mediaPlayerRepository = Creator.provideMediaPlayerRepository()
    private val mediaPlayer = Creator.provideMediaPlayerInteractor()

    //    private val mediaPlayer = MediaPlayerController()
    lateinit var trackItem: Track

    private var runnable: Runnable? = null
    lateinit var binding: ActivityPlayerBinding

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

        binding.playerButtonBack.setOnClickListener {
            finish()
        }
//        val intent = intent
//        val trackItemGson = intent.getStringExtra(TRACK_ITEM_KEY)
//        trackItem = gson.fromJson<Track>(trackItemGson, Track::class.java)
        trackItem = obtainTrackInstance()
        showTrackDetails(trackItem)
        loadPoster(trackItem)
//        Glide.with(binding.root.context)
//            .load(trackItem.getCoverArtWork())
//            .placeholder(R.drawable.vector_empty_album_placeholder)
//            .fitCenter()
//            .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.small_corner_radius)))
//            .into(binding.poster)
//
//        binding.songName.text = trackItem.trackName
//        binding.bandName.text = trackItem.artistName
//        binding.timePlayed.text = "0:30"
//        binding.tracklengthTime.text =
//            DateFormatter.timeFormatter.format(trackItem.trackTimeMillis)
//        binding.albumTitle.text = trackItem.collectionName
//        binding.albumYear.text =
//            DateFormatter.yearFormatter.format(trackItem.trackTimeMillis)
//        binding.trackGenre.text = trackItem.primaryGenreName
//        binding.trackCountry.text = trackItem.country


//        mediaPlayer.preparePlayer(trackItem.previewUrl, binding.stopPlayerButton, handler)
        mediaPlayer.preparePlayer(trackItem.previewUrl)
        binding.stopPlayerButton.setOnClickListener {
//            mediaPlayer.playBackControl(binding.timePlayed)
            if (!mediaPlayer.isPlaying()) {
                startPlayer()
            } else {
                pausePlayer()
            }
        }
    }

    private fun obtainTrackInstance(): Track {
        val intent = intent
        val trackItemGson = intent.getStringExtra(TRACK_ITEM_KEY)
        val trackItem = gson.fromJson<Track>(trackItemGson, Track::class.java)
        return trackItem
    }

    private fun loadPoster(trackItem: Track) {
        Glide.with(binding.root.context)
            .load(trackItem.getCoverArtWork())
            .placeholder(R.drawable.vector_empty_album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.small_corner_radius)))
            .into(binding.poster)
    }


    private fun startPlayer() {
        mediaPlayer.playerStart()
        showTimeCountDown()
        binding.stopPlayerButton.isChecked = true
    }

    private fun pausePlayer() {
        mediaPlayer.playerPause()
        val currentRunnable = runnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
        binding.stopPlayerButton.isChecked = false
    }

    private fun showTimeCountDown() {
        val currentRunnable = runnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
        val newTimerRunnable = object : Runnable {
            override fun run() {
                binding.timePlayed.text =
                    DateFormatter.timeFormatter.format(mediaPlayer.getCurrentPosition())
                handler.postDelayed(this, TIMER_DELAY)
            }
        }

        this.runnable = newTimerRunnable

        handler.postDelayed(newTimerRunnable, TIMER_DELAY)

        mediaPlayer.setOnCompleteListener(
            object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp: MediaPlayer?) {
                    handler.removeCallbacks(newTimerRunnable)
                    binding.timePlayed.text = DateFormatter.timeFormatter.format(0)
                    binding.stopPlayerButton.isChecked = false
                }

            }
        )
    }

    private fun showTrackDetails(trackItem: Track) {
        binding.songName.text = trackItem.trackName
        binding.bandName.text = trackItem.artistName
        binding.timePlayed.text = "0:30"
        binding.tracklengthTime.text =
            DateFormatter.timeFormatter.format(trackItem.trackTimeMillis)
        binding.albumTitle.text = trackItem.collectionName
        binding.albumYear.text =
            DateFormatter.yearFormatter.format(trackItem.trackTimeMillis)
        binding.trackGenre.text = trackItem.primaryGenreName
        binding.trackCountry.text = trackItem.country
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.releasePlayer()
        val currentRunnable = runnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
    }

    override fun onPause() {
        super.onPause()
        val currentRunnable = runnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
        mediaPlayer.playerPause()

    }

}