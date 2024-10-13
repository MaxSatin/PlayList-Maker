package com.practicum.playlistmaker.sharing.domain.repository

interface ActionRepository {
    fun share(text: String)
    fun callSupport(mail: Array<String>, subject: String, text: String)
    fun showUserAgreement(uri: String)
}