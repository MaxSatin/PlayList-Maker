package com.practicum.playlistmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayerBinding
    private val gson = Gson()

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


    }


}