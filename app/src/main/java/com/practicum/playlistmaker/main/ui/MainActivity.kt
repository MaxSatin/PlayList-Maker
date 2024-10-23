package com.practicum.playlistmaker.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.ui.SettingsActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.player.ui.MediaLibraryActivity
import com.practicum.playlistmaker.search.ui.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchButton = findViewById<Button>(R.id.buttonSearchMainActivity)
        val mediaLibraryButton = findViewById<Button>(R.id.buttonMediaLibraryMainActivity)
        val settingsButton = findViewById<Button>(R.id.buttonSettingsMainActivity)

        searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }


        mediaLibraryButton.setOnClickListener {
            startActivity(Intent(this, com.practicum.playlistmaker.medialibrary.ui.activity.MediaLibraryActivity::class.java))
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}