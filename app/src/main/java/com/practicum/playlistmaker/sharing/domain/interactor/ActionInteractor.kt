package com.practicum.playlistmaker.sharing.domain.interactor

interface ActionInteractor {
    fun share(text: String)
    fun callSupport(mail: Array<String>, subject: String, text: String)
    fun showUserAgrement(uri: String)
}