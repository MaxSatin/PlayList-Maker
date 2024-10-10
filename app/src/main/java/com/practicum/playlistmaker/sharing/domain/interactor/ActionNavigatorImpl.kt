package com.practicum.playlistmaker.sharing.domain.interactor

import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R

class ActionNavigatorImpl() : ActionNavigator {
    override fun share(text: String): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
    }

    override fun callSupport(mail: Array<String>, subject: String, text: String): Intent {
        return Intent().apply {
            action = Intent.ACTION_SENDTO
            setData(Uri.parse("mailto:"))
            putExtra(
                Intent.EXTRA_EMAIL,
                mail
            )
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
    }

    override fun showUserAgrement(uri: String): Intent {
       return Intent().apply {
            action = Intent.ACTION_VIEW
            setData(Uri.parse(uri))
        }
    }
}