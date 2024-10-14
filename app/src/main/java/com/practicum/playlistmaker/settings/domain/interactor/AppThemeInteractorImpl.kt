package com.practicum.playlistmaker.settings.domain.interactor

import com.practicum.playlistmaker.settings.domain.repository.AppThemeRepository

class AppThemeInteractorImpl(
    private val appThemeRepository: AppThemeRepository
) : AppThemeInteractor {


    override fun checkIsDarkThemeOn(): Boolean {
        return appThemeRepository.checkIsDarkThemeOn()
    }

    override fun setAppTheme() {
        appThemeRepository.setAppTheme()
    }

    override fun switchAppTheme(isDarkModeEnabled: Boolean) {
        appThemeRepository.switchAppTheme(isDarkModeEnabled)
    }
}

