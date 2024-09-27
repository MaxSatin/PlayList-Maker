package com.practicum.playlistmaker.settings.domain.interactor

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.repository.AppThemeRepository

class AppThemeInteractorImpl(
    private val appThemeRepository: AppThemeRepository
) : AppThemeInteractor {

    private var isDarkTheme = appThemeRepository.checkIsDarkThemeOn()

    override fun isDarkThemeOn(): Boolean {
        return appThemeRepository.checkIsDarkThemeOn()
    }
    override fun setAppTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun switchAppTheme(isDarkModeEnabled: Boolean) {
        isDarkTheme = isDarkModeEnabled
        appThemeRepository.setDarkTheme(isDarkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

