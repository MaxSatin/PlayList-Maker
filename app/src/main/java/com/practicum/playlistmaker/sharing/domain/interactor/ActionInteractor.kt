package com.practicum.playlistmaker.sharing.domain.interactor

import android.content.Intent

interface ActionInteractor {
    fun share(text: String): Intent
    fun callSupport(mail: Array<String>, subject: String, text: String): Intent
    fun showUserAgrement(uri: String): Intent
}