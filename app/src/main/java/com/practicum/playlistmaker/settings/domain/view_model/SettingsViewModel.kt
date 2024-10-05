package com.practicum.playlistmaker.settings.domain.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.settings.domain.interactor.AppThemeInteractor

class SettingsViewModel(
    application: Application,
    ) : AndroidViewModel(application) {

    private val appThemeInteractor: AppThemeInteractor =
        Creator.provideAppThemeInteractor(getApplication())

    private val isDarkThemeOnLiveData = MutableLiveData<Boolean>()
    fun getIsDarkThemeOnLiveData(): LiveData<Boolean> = isDarkThemeOnLiveData

    init {
        isDarkThemeOnLiveData.value = appThemeInteractor.isDarkThemeOn()
    }

    fun switchTheme(isDarkModeEnabled: Boolean) {
        appThemeInteractor.switchAppTheme(isDarkModeEnabled)
        isDarkThemeOnLiveData.value = isDarkModeEnabled
    }

    companion object {
        fun getSettingsViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    this[APPLICATION_KEY] as Application,
                )
            }
        }
    }
}