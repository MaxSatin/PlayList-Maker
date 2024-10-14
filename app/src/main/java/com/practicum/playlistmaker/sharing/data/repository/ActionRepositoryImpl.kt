package com.practicum.playlistmaker.sharing.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.sharing.domain.repository.ActionRepository

class ActionRepositoryImpl(
    private val context: Context,
) : ActionRepository {

    override fun share(text: String) {
        context.startActivity(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    override fun callSupport(mail: Array<String>, subject: String, text: String) {
        context.startActivity(
            Intent().apply {
                action = Intent.ACTION_SENDTO
                setData(Uri.parse("mailto:"))
                putExtra(
                    Intent.EXTRA_EMAIL,
                    mail
                )
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, text)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    override fun showUserAgreement(uri: String) {
        context.startActivity(
            Intent().apply {
            action = Intent.ACTION_VIEW
            setData(Uri.parse(uri))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}
