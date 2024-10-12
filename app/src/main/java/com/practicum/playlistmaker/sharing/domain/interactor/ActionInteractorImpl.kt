package com.practicum.playlistmaker.sharing.domain.interactor

import android.content.Intent
import com.practicum.playlistmaker.sharing.domain.repository.ActionRepository

class ActionInteractorImpl(
    private val actionRepository: ActionRepository
): ActionInteractor {
    override fun share(text: String): Intent {
        return actionRepository.share(text)
    }

    override fun callSupport(mail: Array<String>, subject: String, text: String): Intent {
       return actionRepository.callSupport(mail, subject, text)
    }

    override fun showUserAgrement(uri: String): Intent {
        return actionRepository.showUserAgrement(uri)
    }
}