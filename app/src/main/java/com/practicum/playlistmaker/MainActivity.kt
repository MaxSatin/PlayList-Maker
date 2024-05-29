package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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