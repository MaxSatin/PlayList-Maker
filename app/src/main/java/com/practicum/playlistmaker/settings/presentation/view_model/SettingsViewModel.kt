package com.practicum.playlistmaker.settings.presentation.view_model

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.settings.domain.interactor.AppThemeInteractor
import com.practicum.playlistmaker.sharing.domain.interactor.ActionInteractor

class SettingsViewModel(
    application: Application,
    private val appThemeInteractor: AppThemeInteractor,
    private val actionInteractor: ActionInteractor
    ) : AndroidViewModel(application) {


    private val isDarkThemeOnLiveData = MutableLiveData<Boolean>()
    fun getIsDarkThemeOnLiveData(): LiveData<Boolean> = isDarkThemeOnLiveData

    init {
        isDarkThemeOnLiveData.value = appThemeInteractor.checkIsDarkThemeOn()
    }

    fun switchTheme(isDarkModeEnabled: Boolean) {
        appThemeInteractor.switchAppTheme(isDarkModeEnabled)
        isDarkThemeOnLiveData.value = isDarkModeEnabled
    }

    fun share(text: String): Intent {
        return actionInteractor.share(text)
    }
    fun callSupport(mail: Array<String>, subject: String, text: String): Intent{
        return actionInteractor.callSupport(mail, subject, text)
    }
    fun showUserAgrement(uri: String): Intent {
        return actionInteractor.showUserAgrement(uri)
    }

}