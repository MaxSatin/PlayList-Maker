package com.practicum.playlistmaker.sharing.domain.repository

import android.content.Intent

interface ActionRepository {
    fun share(text: String): Intent
    fun callSupport(mail: Array<String>, subject: String, text: String): Intent
    fun showUserAgrement(uri: String): Intent
}