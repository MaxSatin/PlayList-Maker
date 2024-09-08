package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.ui.media_library.MediaLibraryActivity
import com.practicum.playlistmaker.ui.search.SearchActivity

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

        /*val searchButtonSetOnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(
                    this@MainActivity, "Нажата кнопка 'Поиск'",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } */
        searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }


        mediaLibraryButton.setOnClickListener {
            /*Toast.makeText(
                this@MainActivity, "Нажата кнопка 'Медиатека'",
                Toast.LENGTH_SHORT
            ).show()*/
            startActivity(Intent(this, MediaLibraryActivity::class.java))
        }

        /*val settingsButtonSetOnClickListener = object : View.OnClickListener {
            override fun onClick (v : View?) {
             Toast.makeText(this@MainActivity,"Нажата кнопка 'Настройки'",
                 Toast.LENGTH_SHORT).show()
            }

        }*/
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}