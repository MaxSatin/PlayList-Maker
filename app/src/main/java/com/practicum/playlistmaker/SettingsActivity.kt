package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<Button>(R.id.buttonSettingsBack)
        buttonBack.setOnClickListener {
            finish()
        }

        val buttonShare = findViewById<Button>(R.id.buttonSettingsShare)
        buttonShare.setOnClickListener {
            val messengerIntent = Intent()
            messengerIntent.action = Intent.ACTION_SEND
            messengerIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareAppText))
            messengerIntent.type = "text/plain"
            startActivity(Intent(messengerIntent))
        }

        val buttonSupport = findViewById<Button>(R.id.buttonSettingsSupport)
        buttonSupport.setOnClickListener {
            val mailIntent = Intent()
            mailIntent.action = Intent.ACTION_SENDTO
            mailIntent.setData(Uri.parse("mailto:"))
            mailIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.practicum_support_mail))
            )
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.SupportTextHeader))
            mailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.SupportTextBody))
            startActivity(Intent(mailIntent))
        }

        val buttonUserAgreement = findViewById<Button>(R.id.buttonSettingsUserAgreement)
        buttonUserAgreement.setOnClickListener {
            val browserIntent = Intent()
            browserIntent.action = Intent.ACTION_VIEW
            browserIntent.setData(Uri.parse(getString(R.string.Offer)))
            startActivity(Intent(browserIntent))
        }
    }

}