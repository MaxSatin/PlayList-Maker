package com.practicum.playlistmaker.sharing.domain.interactor

import android.content.Intent
import com.practicum.playlistmaker.sharing.domain.model.Mail

interface ActionNavigator {
    fun share(text: String): Intent
    fun callSupport(mail: Array<String>, subject: String, text: String): Intent
    fun showUserAgrement(uri: String): Intent
}