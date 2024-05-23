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
            val shareTextMessage = getString(R.string.ShareAppText)
            val messengerIntent = Intent()
            messengerIntent.action = Intent.ACTION_SEND
            messengerIntent.putExtra(Intent.EXTRA_TEXT, shareTextMessage)
            messengerIntent.type = "text/plain"
            startActivity(Intent(messengerIntent))
        }

        val buttonSupport = findViewById<Button>(R.id.buttonSettingsSupport)
        buttonSupport.setOnClickListener {
            val supportTextMessageHeader = getString(R.string.SupportTextHeader)
            val supportTextMessageBody = getString(R.string.SupportTextBody)
            val mailIntent = Intent()
            mailIntent.action = Intent.ACTION_SENDTO
            mailIntent.setData(Uri.parse("mailto:"))
            mailIntent.putExtra(Intent.EXTRA_EMAIL, "praktikum@support.yandex.ru")
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, supportTextMessageHeader)
            mailIntent.putExtra(Intent.EXTRA_TEXT, supportTextMessageBody)
            startActivity(Intent(mailIntent))
        }

        val buttonUserAgreement = findViewById<Button>(R.id.buttonSettingsUserAgreement)
        buttonUserAgreement.setOnClickListener {
            val userOfferText = getString(R.string.Offer)
            val browserIntent = Intent ()
            browserIntent.action = Intent.ACTION_VIEW
            browserIntent.setData(Uri.parse(userOfferText))
            startActivity(Intent(browserIntent))
        }
    }

}