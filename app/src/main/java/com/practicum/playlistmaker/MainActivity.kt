package com.practicum.playlistmaker

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
        val mediatekaButton = findViewById<Button>(R.id.buttonMediatekaMainActivity)
        val settingsButton = findViewById<Button>(R.id.buttonSettingsMainActivity)

        val searchButtonSetOnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(
                    this@MainActivity, "Нажата кнопка 'Поиск'",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        searchButton.setOnClickListener(searchButtonSetOnClickListener)

        mediatekaButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажата кнопка 'Медиатека'",
                Toast.LENGTH_SHORT
            ).show()
        }

        val settingsButtonSetOnClickListener = object : View.OnClickListener {
            override fun onClick (v : View?) {
             Toast.makeText(this@MainActivity,"Нажата кнопка 'Настройки'",
                 Toast.LENGTH_SHORT).show()
            }

        }
        settingsButton.setOnClickListener(settingsButtonSetOnClickListener)
    }
}