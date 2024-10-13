package com.practicum.playlistmaker.sharing.domain.interactor

import com.practicum.playlistmaker.sharing.domain.repository.ActionRepository

class ActionInteractorImpl(
    private val actionRepository: ActionRepository
): ActionInteractor {
    override fun share(text: String) {
        actionRepository.share(text)
    }

    override fun callSupport(mail: Array<String>, subject: String, text: String) {
       actionRepository.callSupport(mail, subject, text)
    }

    override fun showUserAgrement(uri: String) {
        actionRepository.showUserAgreement(uri)
    }
}