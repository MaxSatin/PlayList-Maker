package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val sharedPrefs by lazy {
        getSharedPreferences(App.APP_THEME, Context.MODE_PRIVATE)
    }

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

        val buttonBack = findViewById<Button>(R.id.buttonSettingsBack)
        buttonBack.setOnClickListener {
            finish()
        }


        val themeSwitсher = findViewById<SwitchMaterial>(R.id.themeSwither)
        themeSwitсher.isChecked = sharedPrefs.getBoolean(App.IS_DARK_MODE_ON_KEY, false)

        themeSwitсher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val buttonShare = findViewById<FrameLayout>(R.id.buttonSettingsShare)
        buttonShare.setOnClickListener {
            val messengerIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareAppText))
                type = "text/plain"
            }
            startActivity(messengerIntent)
        }

        val buttonSupport = findViewById<Button>(R.id.buttonSettingsSupport)
        buttonSupport.setOnClickListener {
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

        val buttonUserAgreement = findViewById<Button>(R.id.buttonSettingsUserAgreement)
        buttonUserAgreement.setOnClickListener {
            val browserIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                setData(Uri.parse(getString(R.string.Offer)))
            }
            startActivity(browserIntent)
        }
    }

}