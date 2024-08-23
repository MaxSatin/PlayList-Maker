package com.practicum.playlistmaker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayerBinding
    private val handler = Handler(Looper.getMainLooper())
    private val gson = Gson()
    private val mediaPlayer = MediaPlayerController()

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

        binding.playerButtonBack.setOnClickListener{
            finish()
        }
        val intent = intent
        val trackItemGson = intent.getStringExtra("trackItem")
        val trackItem = gson.fromJson<CurrentTrack>(trackItemGson, CurrentTrack::class.java)

        Glide.with(binding.root.context)
            .load(trackItem.getCoverArtWork())
            .placeholder(R.drawable.vector_empty_album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.small_corner_radius)))
            .into(binding.poster)

        binding.songName.text = trackItem.trackName
        binding.bandName.text = trackItem.artistName
        binding.timePlayed.text = "0:30"
        binding.tracklengthTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackItem.trackTimeMillis)
        binding.albumTitle.text = trackItem.collectionName
        binding.albumYear.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(trackItem.trackTimeMillis)
        binding.trackGenre.text = trackItem.primaryGenreName
        binding.trackCountry.text = trackItem.country


        mediaPlayer.createMediaPlayer()
        mediaPlayer.preparePlayer(trackItem.previewUrl, binding.stopPlayerButton)
        binding.stopPlayerButton.setOnClickListener{
            mediaPlayer.playBackControl()
            mediaPlayer.playTimeCountDown(binding.timePlayed, handler)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.releaseMediaPlayer()
        mediaPlayer.removeRunnableCallBacks(handler)
    }


}