package com.practicum.playlistmaker.sharing.domain.interactor

import android.content.Intent

interface ActionNavigator {
    fun share(text: String): Intent
    fun callSupport(mail: Array<String>, subject: String, text: String): Intent
    fun showUserAgrement(uri: String): Intent
}