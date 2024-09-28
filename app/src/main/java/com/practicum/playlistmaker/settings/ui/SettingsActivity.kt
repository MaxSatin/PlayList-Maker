package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val appThemeInteractor by lazy { Creator.provideAppThemeInteractor(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonSettingsBack.setOnClickListener {
            finish()
        }

        binding.themeSwither.isChecked = appThemeInteractor.isDarkThemeOn()

        binding.themeSwither.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        binding.buttonSettingsShare.setOnClickListener {
            val messengerIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareAppText))
                type = "text/plain"
            }
            startActivity(messengerIntent)
        }

        binding.buttonSettingsSupport.setOnClickListener {
            val mailIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                setData(Uri.parse("mailto:"))
                putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(getString(R.string.practicum_support_mail))
                )
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.SupportTextHeader))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.SupportTextBody))
            }
            startActivity(mailIntent)
        }

        binding.buttonSettingsUserAgreement.setOnClickListener {
            val browserIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                setData(Uri.parse(getString(R.string.Offer)))
            }
            startActivity(browserIntent)
        }
    }

}